package org.fugerit.java.jsonutil;

import org.fugerit.java.core.function.SafeFunction;

import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;

public class SampleObfuscateFun implements Function<Object, String>  {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUMBERS = "0123456789";

    private IntFunction<Integer> randomizer;

    public SampleObfuscateFun(IntFunction<Integer> randomizer) {
        this.randomizer = randomizer;
    }

    @Override
    public String apply(Object v) {
        return SafeFunction.get( () -> {
            String res = ProcessPropertyObfuscate.DEFAULT_OBFUSCATE_FIXED;
            if ( v != null ) {
                StringBuilder sb = new StringBuilder();
                String tmp = String.valueOf( v );
                for ( int k=0; k<tmp.length(); k++ ) {
                    char c = tmp.charAt(k);
                    if ( Character.isAlphabetic( c ) ) {
                        sb.append( LETTERS.charAt( randomizer.apply( LETTERS.length() ) ) );
                    } else if ( Character.isDigit( c ) ) {
                        sb.append( NUMBERS.charAt( randomizer.apply( NUMBERS.length() ) ) );
                    } else {
                        sb.append( c );
                    }
                }
                res = sb.toString();
            }
            return res;
        } );
    }

}


class DefaultRandomizer implements IntFunction<Integer> {

    public DefaultRandomizer() {
        this.random = new Random( System.currentTimeMillis() );
    }

    private Random random;

    @Override
    public Integer apply(int bound) {
        return random.nextInt( bound );
    }

}