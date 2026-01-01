package com.nkoder.expense_tracker.security;

import com.nkoder.expense_tracker.model.AuthProvider;
import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(UserRepository userRepository,
                                JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        System.out.println("OAUTH SUCCESS HANDLER HIT");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String providerId = oAuth2User.getAttribute("sub");

        User user = userRepository.
                findByUsernameAndProvider(email, AuthProvider.GOOGLE)

                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername(email);
                    u.setProvider(AuthProvider.GOOGLE);
                    u.setProviderId(providerId);
                    u.setRole("USER");
                    return userRepository.save(u);
                });

        //issUe  JWT
        String jwt = jwtUtil.generateToken(user.getUsername());

        // Redirect to frontend with JWT
        response.sendRedirect(
                "http://localhost:3000/oauth-success?token=" + jwt
        );
    }
}
