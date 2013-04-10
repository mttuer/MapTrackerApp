
public class gini {
	public static void main(String[] args){
		int posSamples = 1;
		double total = 6;
		double p = posSamples/total;
		
		double ent = -p*Math.log(p)/Math.log(2) - (1-p)*Math.log(1-p)/Math.log(2);
		System.out.println(ent);
	} 
}
