package edu.banda.coel.server.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.banda.core.domain.um.User;

/**
 * This class represents the basic "user" object that allows for authentication
 * and user management.  It implements Spring Security's UserDetails interface.
 */
public class SecurityUserDO extends User implements UserDetails {

	/**
	 * Recognizes authorities
	 */
	private enum Authority {
		ROLE_USER,
		ROLE_ADMIN,
		ROLE_SUPERVISOR
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		for (Authority authority : Authority.values()) {
			authList.add(new GrantedAuthorityImpl(authority.toString()));
		}
		return authList;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return !isCredentialsExpired();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.springframework.security.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return isAccountEnabled();
	}
}