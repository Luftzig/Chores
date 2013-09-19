package il.ac.huji.chores;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoav Luft
 * Email: Yoav.luft@gmail.com
 * Date: 14/09/13
 */
public class CoinsGraphView  extends View {

    private ShapeDrawable[] bars;
    // Width and Height use UPPERCASE to signify that they are to be treated as constants
    private final int WIDTH;
    private final int HEIGHT;
    private ShapeDrawable testDrawable;
    private int coinsRange;
    private List<Roommate> roommates;
    private AttributeSet attributes;

    public CoinsGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attributes = attrs;
        WIDTH = getWidth();
        HEIGHT = getHeight();
        initialize();
    }

    public CoinsGraphView(Context context) {
        super(context);

        WIDTH = getMeasuredWidthAndState();
        HEIGHT = getHeight();
        initialize();
    }

    private void initialize() {
        try {
            List<String> roommatesNames = null;
            roommatesNames = ApartmentDAL.getApartmentRoommates(RoommateDAL.getApartmentID());
            this.roommates = getRoommates(roommatesNames);
        } catch (UserNotLoggedInException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FailedToGetRoommateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        calculateMaxDiff(getCoinsList(roommates));
/*        int numBars = roommates.size() > 0? roommates.size() : 1;
        int horizontalSize = WIDTH / numBars / 2;
        int horizontalSpace = WIDTH / numBars / 4;
        bars = new ShapeDrawable[numBars];
        bars[0] = new ShapeDrawable(new RectShape());
        bars[0].setBounds(horizontalSpace, 0, horizontalSize, HEIGHT);
        bars[0].getPaint().setColor(0xff000000);*/

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;

        testDrawable = new ShapeDrawable(new OvalShape());
        testDrawable.getPaint().setColor(0xff74AC23);
        testDrawable.setBounds(x, y, x + width, y + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        testDrawable.draw(canvas);
        int numBars = roommates.size() > 0? roommates.size() : 1;
        int horizontalSize = getWidth() / numBars / 2;
        int horizontalSpace = getWidth() / numBars / 4;
        bars = new ShapeDrawable[numBars];
        bars[0] = new ShapeDrawable(new RectShape());
        bars[0].setBounds(horizontalSpace, 0, horizontalSize, getHeight());
        bars[0].getPaint().setColor(0xff000000);
        bars[0].draw(canvas);
    }

    private void calculateMaxDiff(List<Integer> coins) {
        int maxDiff = 0;
        for (int i = 0; i < coins.size(); i++) {
            for (int j = i + 1; j < coins.size(); j++) {
                if (Math.abs(coins.get(i) - coins.get(j)) > maxDiff) {
                    maxDiff = Math.abs(coins.get(i) - coins.get(j));
                }
            }
        }
        coinsRange = maxDiff;
    }

    private List<Integer> getCoinsList(List<Roommate> roommates) {
        List<Integer> result = new ArrayList<Integer>(roommates.size());
        for (Roommate roommate : roommates) {
            result.add(roommate.getCoinsCollected());
        }
        return result;
    }

    private List<Roommate> getRoommates(List<String> roommates) throws FailedToGetRoommateException {
        List<Roommate> result = new ArrayList(roommates.size());
        FailedToGetRoommateException e;

        for (String roommateId : roommates) {
            Roommate roommateById = RoommateDAL.getRoommateById(roommateId);
            if (roommateById != null) {
                result.add(roommateById);
            }
        }
        return result;
    }
}
