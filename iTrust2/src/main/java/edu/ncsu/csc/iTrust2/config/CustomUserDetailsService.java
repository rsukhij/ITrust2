package edu.ncsu.csc.iTrust2.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.UserService;

@Service ( "customUserDetailsService" )
public class CustomUserDetailsService implements UserDetailsService {

    static final Logger logger = LoggerFactory.getLogger( CustomUserDetailsService.class );

    @Autowired
    private UserService userService;

    @Override
    @Transactional ( readOnly = true )
    public UserDetails loadUserByUsername ( final String ssoId ) throws UsernameNotFoundException {
        final User user = (User) userService.findById( ssoId );
        logger.info( "User : {}", user );
        if ( user == null ) {
            logger.info( "User not found" );
            throw new UsernameNotFoundException( "Username not found" );
        }
        return new org.springframework.security.core.userdetails.User( user.getId(), user.getPassword(), true, true,
                true, true, getGrantedAuthorities( user ) );
    }

    private List<GrantedAuthority> getGrantedAuthorities ( final User user ) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for ( final Role userProfile : user.getRoles() ) {
            logger.info( "UserProfile : {}", userProfile );
            authorities.add( new SimpleGrantedAuthority( userProfile.toString() ) );
        }
        logger.info( "authorities : {}", authorities );
        return authorities;
    }

}
