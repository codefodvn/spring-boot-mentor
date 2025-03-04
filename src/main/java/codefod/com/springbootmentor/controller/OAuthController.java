package codefod.com.springbootmentor.controller;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuthController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/github-url")
    public String getGitHubOAuthUrl() {
        // Retrieve GitHub client registration
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(
                "github");

        // Build OAuth2 authorization request URI
        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(clientRegistration.getClientId())
                .redirectUri(clientRegistration.getRedirectUri())
                .scope(String.valueOf(clientRegistration.getScopes()))
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .state("state")  // Optional, use to protect against CSRF
                .build();

        return authorizationRequest.getAuthorizationUri();
    }
}

