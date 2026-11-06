package auth;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class GoogleAuthService {

    private static final List<String> SCOPES = Arrays.asList("openid", "profile", "email");

    public GoogleIdToken.Payload loginWithGoogle(String clientId, String clientSecret) throws Exception {

        // 1. Client-Daten vorbereiten
        GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
        web.setClientId(clientId);
        web.setClientSecret(clientSecret);
        GoogleClientSecrets secrets = new GoogleClientSecrets().setWeb(web);

        // 2. Authorization Flow aufbauen
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                secrets,
                SCOPES
        ).build();

        // 3. Lokalen Empfänger auf Port 8080 starten
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, receiver);

        // 4. Login-URL generieren & Browser explizit öffnen
        String redirectUri = receiver.getRedirectUri();
        String url = flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();

        System.out.println("Öffne Browser mit URL: " + url);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Bitte öffne folgenden Link manuell im Browser:\n" + url);
        }

        // 5. Auf den Callback-Code warten
        String code = app.getReceiver().waitForCode();

        // 6. Code gegen Tokens tauschen
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        // Server nach Erhalt des Codes wieder stoppen
        receiver.stop();

        // 7. ID-Token parsen und Payload zurückgeben
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        return idToken.getPayload();
    }
}