package org.fugerit.java.jsonutil;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

@Slf4j
public class  ProcessPropertyObfuscate implements  ProcessProperty {

    public static final String DEFAULT_OBFUSCATE_FIXED = "***";

    public static final Function <Object, String> SAMPLE_OBFUSCATE_FUN = new SampleObfuscateFun( new DefaultRandomizer() );

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
    public void processProperty(Map<String, Object> jsonRoot, String path, Map<String, Object> jsonParent, String key, Object value) {
        if ( this.pNames.contains( key ) ) {
            String obfuscatedValue = this.obfuscateFun.apply( value );
            log.trace( "obfuscating value for key {} -> {}", key, obfuscatedValue );
            jsonParent.put( key, obfuscatedValue );
        }
    }
}
