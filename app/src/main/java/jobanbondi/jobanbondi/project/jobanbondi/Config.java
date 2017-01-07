package jobanbondi.jobanbondi.project.jobanbondi;

/**
 * Created by toukir on 11/20/16.
 */

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://www.dogmatt.com/Project21/file_upload.php";
    public static final String GET_MY_FILES_URL = "http://www.dogmatt.com/Project21/getMyfiles.php?user_id=";
    public static final String POST_COMMENT = "http://www.dogmatt.com/Project21/post_comment.php";
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final String GET_COMMENT = "http://www.dogmatt.com/Project21/getAllComment.php?user_id=";
    public static final String COMMENT_VIDEO_ID = "&video_id=";
    public static final String USER_NAME = "user_name";
    public static final String USER_COMMENT = "user_comment";
    public static final String LAST_MONTH_DATA_URL = "http://www.dogmatt.com/Project21/json_last_month.php";
    public static final String LAST_WEEk_DATA_URL = "http://www.dogmatt.com/Project21/json_last_week.php";
    public static final String POST_LIKE = "http://www.dogmatt.com/Project21/post_like.php";

    //For Login
    // Server user login url
    public static String URL_LOGIN = "http://www.dogmatt.com/Project21/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://www.dogmatt.com/Project21/android_login_api/register.php";

}
