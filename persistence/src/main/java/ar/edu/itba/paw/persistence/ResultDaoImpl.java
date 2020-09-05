package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Result;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class ResultDaoImpl implements ResultDao {

    @Override
    public Result findById(long id) {
        return new Result(Date.valueOf("2020-05-20"),"Michael B. Jordan","A6-23QE125231",null,null);
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        ArrayList<Result> results = new ArrayList<>();
        Result resultTemp = new Result(Date.valueOf("2020-05-20"),"Michael B. Jordan","A6-23QE125231",null,null);
        results.add(resultTemp);
        resultTemp = new Result(Date.valueOf("2020-05-20"),"Jhon Elway","A6-23QE125161",null,null);
        results.add(resultTemp);
        return results;
    }
}
