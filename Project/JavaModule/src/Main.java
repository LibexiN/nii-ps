import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Calculation{
    private Integer mu;
    private Integer lambda;
    private Integer m;
    private BigDecimal eps;

    private BigDecimal zeroProbability;
    private BigDecimal oneOnProbability;
    private BigDecimal sumProbability;
    private BigDecimal kAvg;
    private BigDecimal rAvg;

    public Integer getMu() {
        return mu;
    }

    public void setMu(Integer mu) {
        this.mu = mu;
    }

    public Integer getLambda() {
        return lambda;
    }

    public void setLambda(Integer lambda) {
        this.lambda = lambda;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public BigDecimal getEps() {
        return eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }

    public BigDecimal getZeroProbability() {
        return zeroProbability;
    }

    public void setZeroProbability(BigDecimal zeroProbability) {
        this.zeroProbability = zeroProbability;
    }

    public BigDecimal getOneOnProbability() {
        return oneOnProbability;
    }

    public void setOneOnProbability(BigDecimal oneOnProbability) {
        this.oneOnProbability = oneOnProbability;
    }

    public BigDecimal getSumProbability() {
        return sumProbability;
    }

    public void setSumProbability(BigDecimal sumProbability) {
        this.sumProbability = sumProbability;
    }

    public BigDecimal getkAvg() {
        return kAvg;
    }

    public void setkAvg(BigDecimal kAvg) {
        this.kAvg = kAvg;
    }

    public BigDecimal getrAvg() {
        return rAvg;
    }

    public void setrAvg(BigDecimal rAvg) {
        this.rAvg = rAvg;
    }

    Calculation() {}

    Calculation(Integer mu, Integer lambda, BigDecimal eps) {
        this.mu = mu;
        this.lambda = lambda;
        this.eps = eps;
    }


    public void resolve(){
        if(mu==null || lambda==null || eps==null)
            return;
        System.out.println("<start>");
        this.m = resolveM().toBigInteger().intValue();
        do{
            //System.out.println("\tm="+m);
            oneOnProbability = resolveOneOnProbability();
            zeroProbability = resolveZeroProbability();
            sumProbability = resolveSumProbability();
            kAvg = resolveKavg();
            rAvg = resolveRavg();
            Console.printResults(this);
            m++;
        }
        while(!(rAvg.compareTo(eps)>0));
        System.out.println("<end>");
    }

    private BigDecimal resolveM(){
        return new BigDecimal((lambda/mu)+1).setScale(0);
    }

    private BigDecimal resolveZeroProbability(){
        return new BigDecimal(1).divide(this.oneOnProbability, 1000, RoundingMode.HALF_UP);
    }

    public BigDecimal resolveOneOnProbability(){
        BigDecimal firstExpOperand = new BigDecimal(0);
        for(int i = 0; m >= i; i++){
            BigDecimal a = new BigDecimal(Math.pow(alfa(),i));
            BigDecimal b = fact(i);
            BigDecimal c = a.divide(b,10,RoundingMode.HALF_UP);
            firstExpOperand = firstExpOperand.add(c);
        }
        BigDecimal d = new BigDecimal(Math.pow(alfa(),m+1));
        BigDecimal e = fact(m);
        BigDecimal f = new BigDecimal(m-alfa());
        BigDecimal g = e.multiply(f);
        BigDecimal secondExpOperand = d.divide(g,10,RoundingMode.HALF_UP);
        return firstExpOperand.add(secondExpOperand);
    }

    public BigDecimal resolveSumProbability(){
        BigDecimal sum = new BigDecimal(0);
        for(int i=1; m >= i; i++){
            BigDecimal a = new BigDecimal(Math.pow(alfa(),i));
            BigDecimal b = fact(i);
            BigDecimal c = a.divide(b, 10, RoundingMode.HALF_UP);
            BigDecimal e = c.multiply(this.zeroProbability);
            sum = sum.add(e);
        }
        return sum;
    }

    public BigDecimal resolveProbability(Integer i){
        BigDecimal a = new BigDecimal(Math.pow(alfa(),i));
        BigDecimal b = fact(i);
        BigDecimal c = a.divide(b, 10, RoundingMode.HALF_UP);
        return c.multiply(this.zeroProbability);
    }

    public BigDecimal resolveKavg(){
        BigDecimal b = new BigDecimal(0);
        for(int i=0; (m-1)>=i; i++){
            BigDecimal a = resolveProbability(i).multiply(new BigDecimal(i));
            b = b.add(a);
        }
        BigDecimal c = new BigDecimal(0);
        for(int i=0; (m-1)>=i; i++){
            c = c.add(resolveProbability(i));
        }
        BigDecimal d = new BigDecimal(m).multiply(new BigDecimal(1).subtract(c));
        return b.add(d);
    }

    public BigDecimal resolveRavg(){
        BigDecimal a = new BigDecimal(Math.pow(alfa(),m+1));
        BigDecimal b = a.multiply(this.zeroProbability);
        BigDecimal c = fact(m-1);
        BigDecimal d = new BigDecimal(Math.pow(m-alfa(),2));
        BigDecimal e = c.multiply(d);
        return b.divide(e,10,RoundingMode.HALF_UP);
    }

    private Double alfa(){
        return (double) (lambda / mu);
    }

    public BigDecimal fact(Integer n){
        BigDecimal result;
        if(n==0){
            return new BigDecimal(1);
        }
        result = new BigDecimal(new BigDecimal(n).multiply(fact(n-1)).toString());
        return result;
    }

    @Override
    public String toString(){
        return "mu: "+mu+"\nlambda: "+lambda+"\nm:"+ m;
    }
}

class Console{
    public static boolean inputSourceData(BufferedReader br, Calculation calc) throws IOException {
        System.out.println("---------------------------------------------------------------------------------\ninput mu: ");
        String mu = br.readLine();
        if(mu.equals("q")) return false;
        else calc.setMu(new Integer(mu));

        System.out.println("input lambda: ");
        String lambda = br.readLine();
        if(lambda.equals("q")) return false;
        else calc.setLambda(new Integer(lambda));

        System.out.println("input eps: ");
        String eps = br.readLine();
        if(eps.equals("q")) return false;
        else calc.setEps(new BigDecimal(eps));
        System.out.println("---------------------------------------------------------------------------------\n");

        return true;
    }

    public static void printResults(Calculation calc){
        System.out.println("\t>>>\tm:\t"+calc.getM());
        System.out.println("\t\t1/Po:\t"+calc.getOneOnProbability().setScale (30, BigDecimal.ROUND_HALF_UP));
        System.out.println("\t\tPo:\t\t"+calc.getZeroProbability().setScale (30, BigDecimal.ROUND_HALF_UP));
        System.out.println("\t\tSum Pi:\t" + calc.getSumProbability().setScale(30, BigDecimal.ROUND_HALF_UP));
        System.out.println("\t\tKavg:\t"+calc.getkAvg().setScale(30, BigDecimal.ROUND_HALF_UP));
        System.out.println("\t\tRavg:\t" + calc.getrAvg().setScale(30, BigDecimal.ROUND_HALF_UP));
        System.out.println();
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //new Calculation().fact(3000);
        do{
            Calculation calc = new Calculation();
            if(!Console.inputSourceData(br, calc))
                break;
            calc.resolve();
        }
        while(true);
    }
}
