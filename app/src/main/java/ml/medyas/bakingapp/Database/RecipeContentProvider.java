package ml.medyas.bakingapp.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import ml.medyas.bakingapp.Classes.RecipeClass;

public class RecipeContentProvider extends ContentProvider {

    public static final String AUTHORITY = "ml.medyas.baknigApp.RecipeContentProvider.provider";
    public static final Uri URI_RECIPE = Uri.parse(
            "content://" + AUTHORITY + "/" + RecipeClass.TABLE_NAME);

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private DaoClass mDao;
    private Context context;
    /** The match code for some items in the Cheese table. */
    private static final int CODE_RECIPE_DIR = 1;

    static {
        MATCHER.addURI(AUTHORITY, RecipeClass.TABLE_NAME, CODE_RECIPE_DIR);
    }

    public RecipeContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        mDao = RecipesRoomDB.getDatabase(context).recipeDao();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_RECIPE_DIR) {
            final Cursor cursor;
            cursor = mDao.getRecipesCursor();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
