package ar.edu.itba.paw.model;

public class Result {
   // private Patient patient;    //se podria sacar de la orden en si
   // Patient seria el User, pero eso aun no existe
   // private Medic solicitant;   //se podria sacar de la orden en si
   // private long order;         //se podria sacar de la orden en si
   // private Institute institute;//se podria sacar de la orden en si
   // private Date date;

    //vamos a hacer referencia al OrderDao que conseguimos del link!!!!!
    private Order order;

    private String resultdescription;
    private Object data;
    private Medic responsible; //doctor?
    private Object signresponsible;


    public Result(final Order order, final String resultdescription, final Object data, final Medic responsible, final Object signresponsible){
        this.order = order;
        this.resultdescription = resultdescription;
        this.data = data;
        this.responsible = responsible;
        this.signresponsible = signresponsible;
    }

    //public long getOrderNumber() {return order.getId();}
    public String getResultDescription(){
        return resultdescription;
    }
    public long getResponsibleLn(){
        return responsible.getLicenceNumber();
    }
    public Object getData(){
        return data;
    }
}
