package app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters.")
    private String login;

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @Size(min = 3, message = "Password must be at least 3 characters.")
    private String password;
    private String email;

    private String confirmationToken; // Add the confirmationToken attribute
    @GeneratedValue(generator = "false")
    private boolean confirmed; // Add the confirmed attribute
    @GeneratedValue(generator = "false")
    private boolean maillogin; // Add the 2FactorAuth attribute

    private String token; // Add the token attribute
    // Implement other UserDetails methods
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tokentime;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }


    public void setMaillogin(boolean maillogin) {
        this.maillogin = maillogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isMaillogin() {
        return maillogin;
    }

    public LocalDateTime getTokentime() {
        return tokentime;
    }

    public void setTokentime(LocalDateTime tokentime) {
        this.tokentime = tokentime;
    }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
