package com.fitnesspartner.security.authentication;

import com.fitnesspartner.constants.UserState;
import com.fitnesspartner.domain.UserRoles;
import com.fitnesspartner.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Users users;

    public Users getUsers() {
        return this.users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Optional<List<UserRoles>> userRoles = Optional.ofNullable(users.getUserRolesList());
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(userRoles.isPresent()) {
            for(UserRoles userRole : userRoles.get()) {
                authorities.add(new SimpleGrantedAuthority(userRole.getRoleName()));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        UserState userState = users.getUserState();
        if (userState.equals(UserState.Enabled)) {
            return true;
        }
        return false;
    }
}
