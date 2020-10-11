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
    public long decode(String path){

        long ret = -1;

        if(path.length() > 20){
            return ret;
        }

        BigInteger pathBigInteger;

        try{
            pathBigInteger = new BigInteger(path,num3);
        }catch (NumberFormatException e){
            return ret;
        }

        pathBigInteger = pathBigInteger.subtract(BigInteger.valueOf(num2));
        if(0 > pathBigInteger.compareTo(BigInteger.ZERO)){
            return ret;
        }
        if(!pathBigInteger.remainder(BigInteger.valueOf(num1)).equals(BigInteger.ZERO)){
            return ret;
        }
        pathBigInteger = pathBigInteger.divide(BigInteger.valueOf(num1));
        if(1 > pathBigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))){
            ret = pathBigInteger.longValue();
        }
        return ret;
    }
}
