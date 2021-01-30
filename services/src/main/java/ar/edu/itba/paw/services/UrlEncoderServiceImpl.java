package ar.edu.itba.paw.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Primary
@Service
public class UrlEncoderServiceImpl implements UrlEncoderService {

    private int num1 = 7;
    private int num2 = 37;
    private int num3 = 17;

    @Override
    public String encode(long id){

        BigInteger pathBigInteger = BigInteger.valueOf(id);
        pathBigInteger = pathBigInteger.multiply(BigInteger.valueOf(num1));
        pathBigInteger = pathBigInteger.add(BigInteger.valueOf(num2));

        return pathBigInteger.toString(num3);
    }

    @Override
    public long decode(String path) throws NumberFormatException{

        if(path.length() > 20){
            throw new NumberFormatException();
        }

        BigInteger pathBigInteger;

        // may throw NumberFormatException
        pathBigInteger = new BigInteger(path,num3);

        pathBigInteger = pathBigInteger.subtract(BigInteger.valueOf(num2));
        if(0 > pathBigInteger.compareTo(BigInteger.ZERO)){
            throw new NumberFormatException();
        }
        if(!pathBigInteger.remainder(BigInteger.valueOf(num1)).equals(BigInteger.ZERO)){
            throw new NumberFormatException();
        }
        pathBigInteger = pathBigInteger.divide(BigInteger.valueOf(num1));
        if(1 > pathBigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))){
            return pathBigInteger.longValue();
        }

        throw new NumberFormatException();
    }
}
