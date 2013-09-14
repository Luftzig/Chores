package il.ac.huji.chores;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesToServer {
	
	

    public static void callFunction(String target, ASyncListener listener, Context context, String... values) {
        List<String> params = new ArrayList<String>(Arrays.asList(values));
        params.add(0, target);
        (new ParseCall(listener, context)).execute(params.toArray(new String[params.size()]));
    }

    private static class ParseCall extends AsyncTask<String, Void, String> {

        private final ASyncListener listener;
        private final String appKey;
        private final String restKey;

        public ParseCall(ASyncListener listener, Context context) {
            this.listener = listener;
            this.appKey = context.getString(R.string.parse_app_id);
            this.restKey = context.getString(R.string.parse_rest_key);
        }

        /**
         * First string is the function name, the following strings will be used as a single key-value pair in the POST
         * method.
         * @param strings
         * @return result, usually JSON as string.
         */
        @Override
        protected String doInBackground(String... strings) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(Constants.PARSE_FUNCTIONS_URL + strings[0]);
            request.setHeader("X-Parse-Application-Id", appKey);
            request.setHeader("X-Parse-REST-API-Key", restKey);
            String jsonAsString = joinToJson(Arrays.copyOfRange(strings, 1, strings.length));
            try {
                StringEntity message = new StringEntity(jsonAsString);
                message.setContentType("application/json");
                request.setEntity(message);
                Log.d("ParseCall", "Sending request: " + request.toString());
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() != 200) {
                    Log.e("ParseCall", "HttpRequest to " + request.getURI() + " failed. Response code: "
                            + response.getStatusLine().getStatusCode());
                    return null;
                }

                StringBuilder responseString = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String s;
                while ((s = reader.readLine()) != null) {
                    responseString.append(s);
                }
                client.getConnectionManager().shutdown();
                return responseString.toString();
            } catch (UnsupportedEncodingException e) {
                Log.w("ParseCall", "failed due to: ", e);
            } catch (ClientProtocolException e) {
                Log.w("ParseCall", "failed due to: ", e);
            } catch (IOException e) {
                Log.w("ParseCall", "failed due to: ", e);
            }
            return null;
        }

        private String joinToJson(String[] strings) {
            StringBuilder builder = new StringBuilder("{");
            for (String str : strings) {
                builder.append(str).append(", ");
            }
            builder.delete(builder.lastIndexOf(", "), builder.length()).append("}");
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            this.listener.onASyncComplete(result);
        }
    }

}
