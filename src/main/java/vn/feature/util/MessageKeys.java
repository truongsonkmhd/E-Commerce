package vn.feature.util;

public class MessageKeys {
    // login
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String LOGIN_FAILED = "Login failed";
    public static final String PHONE_NUMBER_EXISTED = "Phone number existed";
    public static final String PHONE_NUMBER_AND_PASSWORD_FAILED = "Phone number or password incorrect";
    public static final String CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN = "Cannot create account with admin role";

    // register
    public static final String PASSWORD_NOT_MATCH = "Passwords do not match";
    public static final String REGISTER_SUCCESS = "Register successfully";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String TOKEN_EXPIRATION_TIME = "Token expired";
    public static final String NOT_FOUND = "Not found";
    public static final String USER_EXISTED = "User already exists";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String UPDATE_COMMENT_SUCCESS = "Comment updated successfully";
    public static final String COMMENT_INSERT_SUCCESS = "Comment added successfully";

    // validation
    public static final String ERROR_MESSAGE = "An error occurred";
    public static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String RETYPE_PASSWORD_REQUIRED = "Retype password is required";
    public static final String ROLE_ID_REQUIRED = "Role ID is required";
    public static final String PRODUCT_ID_REQUIRED = "Product ID is required";
    public static final String IMAGE_SIZE_REQUIRED = "Image size is required";
    public static final String PRODUCT_TITLE_REQUIRED = "Product title is required";
    public static final String PRODUCT_TITLE_SIZE_REQUIRED = "Product title size is invalid";
    public static final String PRODUCT_PRICE_MIN_REQUIRED = "Minimum product price is required";
    public static final String PRODUCT_PRICE_MAX_REQUIRED = "Maximum product price is required";
    public static final String USER_ID_REQUIRED = "User ID is required";
    public static final String PHONE_NUMBER_SIZE_REQUIRED = "Phone number length is invalid";
    public static final String TOTAL_MONEY_REQUIRED = "Total money is required";
    public static final String ORDER_ID_REQUIRED = "Order ID is required";
    public static final String NUMBER_OF_PRODUCT_REQUIRED = "Number of products is required";
    public static final String CATEGORIES_NAME_REQUIRED = "Category name is required";

    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String USER_ID_LOCKED = "User account is locked";
    public static final String USER_ID_UNLOCKED = "User account is unlocked";
    public static final String REFRESH_TOKEN_SUCCESS = "Token refreshed successfully";

    // token
    public static final String ERROR_REFRESH_TOKEN = "Failed to refresh token";
    public static final String FILES_REQUIRED = "File is required";
    public static final String FILES_IMAGES_SUCCESS = "Images uploaded successfully";
    public static final String FILES_IMAGES_FAILED = "Failed to upload images";
    public static final String FILES_IMAGES_SIZE_FAILED = "Image size is too large";
    public static final String FILES_IMAGES_TYPE_FAILED = "Invalid image type";

    // error get/update/delete
    public static final String MESSAGE_ERROR_GET = "Failed to retrieve data";
    public static final String MESSAGE_UPDATE_GET = "Updated successfully";
    public static final String MESSAGE_DELETE_SUCCESS = "Deleted successfully";
    public static final String MESSAGE_DELETE_FAILED = "Delete failed";

    public static final String RESET_PASSWORD_SUCCESS = "Password reset successfully";
    public static final String RESET_PASSWORD_FAILED = "Password reset failed";

    public static final String CREATE_ORDER_SUCCESS = "Order created successfully";
    public static final String CREATE_ORDER_FAILED = "Failed to create order";
    public static final String CREATE_ORDER_DETAILS_FAILED = "Failed to create order details";
    public static final String CREATE_ORDER_DETAILS_SUCCESS = "Order details created successfully";
    public static final String CREATE_PRODUCT_SUCCESS = "Product created successfully";
    public static final String CREATE_PRODUCT_FAILED = "Failed to create product";
    public static final String CREATE_CATEGORIES_SUCCESS = "Category created successfully";
    public static final String CREATE_CATEGORIES_FAILED = "Failed to create category";
    public static final String UPDATE_CATEGORIES_SUCCESS = "Category updated successfully";
    public static final String UPDATE_CATEGORIES_FAILED = "Failed to update category";
    public static final String DELETE_CATEGORIES_SUCCESS = "Category deleted successfully";
    public static final String DELETE_CATEGORIES_FAILED = "Failed to delete category";
    public static final String DELETE_COMMENT_SUCCESS = "Comment deleted successfully";
    public static final String DELETE_COMMENT_FAILED = "Failed to delete comment";

    public static final String GET_INFORMATION_FAILED = "Failed to get information";
    public static final String GET_INFORMATION_SUCCESS = "Information retrieved successfully";

    public static final String APP_AUTHORIZATION_403 = "Access denied";
    public static final String APP_UNCATEGORIZED_500 = "Internal server error";
    public static final String APP_PERMISSION_DENY_EXCEPTION = "Permission denied";
}
