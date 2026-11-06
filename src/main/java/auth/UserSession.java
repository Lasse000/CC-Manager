package auth;

public class UserSession {
    private static UserSession instance;
    private final int userId;
    private final String email;
    private final String firstName;

    private UserSession(int userId, String email, String firstName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
    }

    public static void init(int userId, String email, String firstName) {
        instance = new UserSession(userId, email, firstName);
    }

    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Kein User eingeloggt!");
        }
        return instance;
    }

    public int getUserId() { return userId; }
    public String getFirstName() { return firstName; }
}