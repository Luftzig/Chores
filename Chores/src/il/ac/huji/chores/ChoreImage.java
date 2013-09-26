package il.ac.huji.chores;

import android.content.Context;

/**
 * @author Yoav Luft
 */
public enum ChoreImage {
    COOK_DINNER(0, R.drawable.cook_dinner),
    DISH_WASHING(1, R.drawable.dish_washing),
    DO_THE_LAUNDRY(2, R.drawable.do_the_laundry),
    DUSTING(3, R.drawable.dusting),
    FLOOR_SWEEPING(4, R.drawable.floor_sweeping),
    FLOOR_WASHING(5, R.drawable.floor_washing),
    GROCERY_SHOPPING(6, R.drawable.grocery_shopping),
    TAKE_OUT_THE_TRASH(7, R.drawable.take_out_the_trash),
    TOILET_CLEANING(8, R.drawable.toilet_cleaning),
    WALK_THE_DOG(9, R.drawable.walk_the_dog);

    private int arrayIndex;
    private int drawableId;

    private ChoreImage(int arrayIndex, int drawableId) {
        this.arrayIndex = arrayIndex;
        this.drawableId = drawableId;
    }

    public String getString(Context context) {
        return context.getResources().getStringArray(R.array.chore_names_array)[arrayIndex];
    }

    public int getDrawableId() {
        return drawableId;
    }

    public static ChoreImage fromString(String name) {
        return ChoreImage.valueOf(name.replace(" ", "_").toUpperCase());
    }
}
