package org.fugerit.java.jsonutil;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Slf4j
public class ProcessPropertyObfuscate implements  ProcessProperty {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUMBERS = "0123456789";

    public static final String DEFAULT_OBFUSCATE_FIXED = "***";

    public static Function <Object, String> SAMPLE_OBFUSCATE_FUN = v -> {
        return SafeFunction.get( () -> {
            String res = DEFAULT_OBFUSCATE_FIXED;
            SecureRandom sr = SecureRandom.getInstanceStrong();
            if ( v != null ) {
                StringBuilder sb = new StringBuilder();
                String tmp = String.valueOf( v );
                for ( int k=0; k<tmp.length(); k++ ) {
                    char c = tmp.charAt(k);
                    if ( Character.isAlphabetic( c ) ) {
                        sb.append( LETTERS.charAt( sr.nextInt( LETTERS.length() ) ) );
                    } else if ( Character.isDigit( c ) ) {
                        sb.append( NUMBERS.charAt( sr.nextInt( NUMBERS.length() ) ) );
                    } else {
                        sb.append( c );
                    }
                }
                res = sb.toString();
            }
            return res;
        } );
    };

    private Function <Object, String> obfuscateFun;

    private Set<String> pNames;

    public ProcessPropertyObfuscate( List<String> propertyNames ) {
        this( DEFAULT_OBFUSCATE_FIXED, propertyNames );
    }

    public ProcessPropertyObfuscate( String obfuscateFixed, List<String> propertyNames ) {
        this.obfuscateFun = v -> obfuscateFixed;
        this.pNames = new HashSet<>( propertyNames );
    }

    public ProcessPropertyObfuscate( Function <Object, String> obfuscateFun, List<String> propertyNames ) {
        this.obfuscateFun = obfuscateFun;
        this.pNames = new HashSet<>( propertyNames );
    }

    @Override
    public void processProperty(LinkedHashMap<String, Object> jsonRoot, String path, LinkedHashMap<String, Object> jsonParent, String key, Object value) {
        if ( this.pNames.contains( key ) ) {
            String obfuscatedValue = this.obfuscateFun.apply( value );
            log.trace( "obfuscating value for key {} -> {}", key, obfuscatedValue );
            jsonParent.put( key, obfuscatedValue );
        }
    }
}
