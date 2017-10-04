package cn.timelives.java.math.numberModels;

import static cn.timelives.java.math.numberModels.FormulaCalculator.DEFAULT_FORMULA_CALCULATOR;
import static cn.timelives.java.utilities.Printer.print;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.timelives.java.math.addableSet.AdditiveSet;
import cn.timelives.java.utilities.ArraySup;;
/**
 * polyCalculator 是用于计算多项式的计算器
 * <p>功能有：
 * <ul>
 * <li>加减
 * <li>乘
 * <li>除
 * <li>整数次乘方
 * <li>整数次开方
 * <li>三角计算
 * <li>指数,对数计算
 * <li>......
 * </ul>
 * <p><b>注意：</b>部分运算由于其特殊性，不能给出多项式答案，会抛出异常
 * 
 * 
 * @author lyc
 *
 */
public class PolyCalculator extends MathCalculatorAdapter<Polynomial>
{
	private FormulaCalculator ca;
	
	
	private static Pair ofVal(long n,long d){
		return new Pair(BigInteger.valueOf(n),BigInteger.valueOf(d));
	}
	
	private static class Pair{
		final BigInteger n,d;
		Pair(BigInteger n,BigInteger d){
			this.n = n;
			this.d = d;
		}
		@Override
		public int hashCode() {
			return n.hashCode() * 31 + d.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pair){
				Pair p = (Pair) obj;
				return n.equals(p.n)&&d.equals(p.d);
			}
			return false;
		}
		@Override
		public String toString() {
			return "[" + n.toString() +","+d.toString()+"]";
		}
		static Pair of(BigInteger[] arr){
			return new Pair(arr[0],arr[1]);
		}
	}
	
	private static void initValue(){
//		try {
//			PolyCalculator.class.getClassLoader().loadClass(Polynomial.class.getName());
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//		}
		
		SIN_VALUE.put(ofVal(0l,1l),Polynomial.ZERO);
		// sin(0) = 0
		SIN_VALUE.put(ofVal(1l,6l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,
				Formula.asFraction(1,2,1)));
		//sin(Pi/6) = 1 / 2
		SIN_VALUE.put(ofVal(1l,4l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,
				Formula.asFraction(1,2,2)));
		//sin(Pi/4) =sqr(2)/2
		SIN_VALUE.put(ofVal(1l,3l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,
				Formula.asFraction(1,2,3)));
		//sin(Pi/3) = sqr(3) / 2
		SIN_VALUE.put(ofVal(1l,2l),Polynomial.ONE);
		//sin(Pi/2) = 1
		
		SIN_VALUE.put(ofVal(1l,12l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,"Sqr6/4-Sqr2/4"));
		//sin(Pi/12) = sqr6/4-sqr2/4
		
		SIN_VALUE.put(ofVal(5l,12l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,"Sqr6/4+Sqr2/4"));
		//sin(Pi/12) = sqr6/4-sqr2/4
		
		TAN_VALUE.put(ofVal(0l,1l),Polynomial.ZERO);
		// tan(0) = 0
		TAN_VALUE.put(ofVal(1l,6l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,
				Formula.asFraction(1,3,3)));
		//tan(Pi/6) = Sqr(3)/3
		TAN_VALUE.put(ofVal(1l,4l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,Formula.ONE));
		//tan(Pi/4) = 1
		TAN_VALUE.put(ofVal(1l,3l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,
				Formula.asFraction(1,1,3)));
		//tan(Pi/3) = Sqr(3)
		
		TAN_VALUE.put(ofVal(1l,12l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,"2-Sqr3"));
		//tan(Pi/12) = 2-Sqr3
		TAN_VALUE.put(ofVal(5l,12l),new Polynomial(DEFAULT_FORMULA_CALCULATOR,"2+Sqr3"));
		//tan(Pi/12) = 2+Sqr3 
		
		for(Entry<Pair,Polynomial> e : SIN_VALUE.entrySet()){
			Pair p = e.getKey();
			ARCSIN_VALUE.put(e.getValue(), new Polynomial(DEFAULT_FORMULA_CALCULATOR,Formula.asFraction(p.n, p.d, BigInteger.ONE)));
		}
		for(Entry<Pair,Polynomial> e : TAN_VALUE.entrySet()){
			Pair p = e.getKey();
			ARCTAN_VALUE.put(e.getValue(), new Polynomial(DEFAULT_FORMULA_CALCULATOR,Formula.asFraction(p.n, p.d, BigInteger.ONE)));
		}
	}
	
	
	/**
	 * SIN_VALUE stores the sin result stored in 0 to Pi/2
	 * <p> through youDaoGongShi , you can get every value of sin cos tan...
	 *  
	 *  
	 */
	public static final Map<Pair,Polynomial> SIN_VALUE=new HashMap<>();
	
	public static final Map<Pair,Polynomial> TAN_VALUE=new HashMap<>();
	
	/**
	 * this Map contains arcsin values 
	 * @see SIN_VALUE 
	 */
	public static final Map<Polynomial,Polynomial> ARCSIN_VALUE=new TreeMap<Polynomial,Polynomial>();
	/**
	 * this Map contains arctan values 
	 * @see TAN_VALUE 
	 */
	public static final Map<Polynomial,Polynomial> ARCTAN_VALUE=new TreeMap<Polynomial,Polynomial>();
	
	private static boolean hasInit= false;
	
	
	
	/**
	 * Default calculator is a calculator whose state is 1
	 */
	public static final PolyCalculator DEFALUT_CALCULATOR = new PolyCalculator(1);
	
	private final Polynomial PI ;
	private final Polynomial E ;
	
	PolyCalculator(int state){
		synchronized (PolyCalculator.class) {
			if(hasInit==false){
				initValue();
				hasInit=true;
			}
		}
		if(state == 1){
			ca = DEFAULT_FORMULA_CALCULATOR;
		}else{
			ca= new FormulaCalculator(state);
		}
		PI = new Polynomial(ca, ca.constantValue(MathCalculator.STR_PI));
		E = new Polynomial(ca, ca.constantValue(MathCalculator.STR_E));
	}
	
	public PolyCalculator(){
		this(1);
	}
	
	/**
	 * this method will return a polynomial that equals to p1+p2
	 * <p>the Adder is this.Calculator
	 * @param p1
	 * @param p2
	 * @return p1+p2
	 */
	@Override
	public Polynomial add(Polynomial p1,Polynomial p2){
		Polynomial result=new Polynomial(ca);
		for(Formula f:p1.getFormulas()){
			result.addFormula(f);
		}
		for(Formula f:p2.getFormulas()){
			result.addFormula(f);
		}
		result.removeZero();
		return result;
	}
	
	/**
	 * this method will return a polynomial that equals to p1-p2
	 * @param p1
	 * @param p2
	 * @return p1-p2
	 */
	@Override
	public Polynomial subtract(Polynomial p1,Polynomial p2){
		Polynomial result=new Polynomial(ca);
		for(Formula f:p1.getFormulas()){
			result.addFormula(f);
		}
		for(Formula f:p2.getFormulas()){
			result.addFormula(f.negate());
		}
		result.removeZero();
		return result;
	}
	
	/**
	 * this method will return a polynomial that equals to {@code -p}
	 * @param p
	 * @return -p
	 */
	@Override
	public Polynomial negate(Polynomial p){
		Polynomial result=new Polynomial(ca);
		for(Formula f:p.getFormulas()){
			result.addFormula(f.negate());
		}
		result.removeZero();
		return result;
	}
	
	/**
	 * this method will return a polynomial that equals to p1*p2
	 * @param p1
	 * @param p2
	 * @return p1*p2
	 */
	@Override
	public Polynomial multiply(Polynomial p1,Polynomial p2){
		Polynomial result=new Polynomial(ca);
		for(Formula f1:p1.getFormulas()){
			for(Formula f2:p2.getFormulas()){
				Formula f = ca.multiply(f1, f2);
				if(f!=Formula.ZERO){
					result.removeZero();
					result.addFormula(f);
				}
			}
		}
//		Printer.print(result);
		return result;
	}
	
	/**
	 * this method will return a polynomial that equals to p1/p2
	 * @param p1
	 * @param p2
	 * @return p1/p2
	 * @throws CannotCalculateException if cannot calculate
	 */
	@Override
	public Polynomial divide(Polynomial p1,Polynomial p2){
		int num = p2.getNumOfFormula();
		if(num==1){
			Polynomial result=new Polynomial(ca);
			Formula devisor = p2.getFormulas().iterator().next(); 
			for(Formula f : p1.getFormulas()){
				result.addFormula(ca.divide(f,devisor));
			}
			result.removeZero();
			return result;
		}
		else{   //    1/(Sqr2-1)
			/* e.g. 1 / (Sqr2 -1 )
			 *     = 1 * (Sqr2 + 1)  / ((Sqr2+1)(Sqr2 -1))
			 *     = (Sqr2 + 1) / (2-1)
			 *     =Sqr2 +1
			 */
			boolean moreComplex = false;
			for(Iterator<Formula> it = p2.getFormulas().iterator(); it.hasNext() ; ){
				if(it.next().getNumOfChar()!=0){
					moreComplex = true;
					break;
				}
			}
			if(moreComplex){
				return handleComplexDivision(p1, p2);
			}else{
				Formula d = p2.getFormulas().iterator().next(); 
				Polynomial mul = subtract(p2, new Polynomial(ca,ca.addEle(d, d)));
				return divide(multiply(p1, mul),this.multiply(mul, p2));
			}
		}
	}
	private Polynomial handleComplexDivision(Polynomial p1,Polynomial p2){
		//Only to solve forms like: k*p1 = p2 where k is a Formula.
		//simplify first an try to find corresponding ,
		Polynomial result = null;
		
		Formula[] f2 = new Formula[0];
		Formula[] f1 = p1.getFormulas().toArray(f2);
		f2 = p2.getFormulas().toArray(f2);
		
		result = divide1(f1,f2);
		if(result != null){
			return result;
		}
		result = divide2(f1,f2);
		if(result != null){
			return result;
		}
		throw new UnsupportedCalculationException();
	}
	
	
	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Polynomial divide2(Formula[] p1, Formula[] p2) {
		//look for a^2-b^2 form:
		if(p1.length != 2 || p2.length!=2){
			return null;
		}
		//extract irrelevant coefficient: how to do it?
		// find gcd formula first.
		Formula gcd1 = Formula.gcdAndDivide(p1);
		Formula gcd2 = Formula.gcdAndDivide(p2);
		Formula k = ca.divide(gcd1, gcd2);
		//search for a^2 and b^2:
		if(p1[0].getSignum() * p1[1].getSignum() > 0){
			return null;
		}
		int toChange = -1;
//		if(p1[0].getSignum()<0){
//			if(p1[1].getSignum()<0){
//				return null;
//			}
//			p1[0] = p1[0].negate();
//			p1[1] = p1[1].negate();
//			k = k.negate();
//		}else{
//			if(p1[1].getSignum()>0){
//				return null;
//			}
//		}
		Formula[] p22 = new Formula[]{ca.square(p2[0]),ca.square(p2[1])};
		if(p22[0].absEquals(p1[0])){
			if(!p22[1].absEquals(p1[1])){
				return null;
			}
			toChange = p1[0].getSignum() > 0 ? 1 : 0;
		}else if(p22[0].absEquals(p1[1])){
			if(!p22[1].absEquals(p1[0])){
				return null;
			}
			toChange = p1[0].getSignum() > 0 ? 0 : 1;
		}else{
			return null;
		}
//		if(p1[0].equals(a2)){
//			if(p1[1].equals(b2)){
//				
//			}
//		}else if(p1[0].equals(b2)){
//			
//		}
		p2[toChange] = p2[toChange].negate();
		p2[0] = ca.multiply(k, p2[0]);
		p2[1] = ca.multiply(k, p2[1]);
		return new Polynomial(ca, p2);
	}

	private Polynomial divide1(Formula[] f1,Formula[] f2){
		if(f1.length!=f2.length){
			return null;
		}
		final int num = f1.length;
		//a simple solution: choose the first formula in p1, tries to find the mapping k and 
		//go on
		
		int[] map = new int[num];
		boolean found = false;
		Formula kf = null;
		for(int i=0;i<num;i++){
			Formula k = ca.divide(f2[i],f1[0]);
			ArraySup.fillArr(map, -1);
			map[0] = i;
			if(findCor(k,f1,f2,map)){
				found = true;
				kf = k;
				break;
			}
		}
		if(!found){
			return null;
		}
		return new Polynomial(ca, kf.reciprocal());
	}
	
	
	/**
	 * Assumed that map[0] = i, which means f1.0<-->f2.i
	 * @param k
	 * @param f1
	 * @param f2
	 * @param map
	 * @return
	 */
	private boolean findCor(Formula k,Formula[] f1,Formula[] f2,int[] map){
		final int len = f1.length;
		boolean[] mapped = new boolean[len];
		mapped[map[0]] = true;
		for(int i=1;i<len;i++){
			boolean found = false;
			Formula kp1 = ca.multiply(k, f1[i]);
			for(int j=0;j<len;j++){
				if(!mapped[j] && f2[j].equals(kp1)){
					mapped[j] = true;
					found = true;
					break;
				}
			}
			if(!found){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	
	public FormulaCalculator getCal(){
		return ca;
	}
	
	/**
	 * this method will calculate p^power only if power is an integer greater or equals 0
	 * <p> While using this method , it should be noticed that power should lager than 0 or equals 0
	 * @param pd : the down part of the exp
	 * @param power : the up part of the exp 
	 * @return a Polynomial that equals to p^power or , in other way , exp(p,powerP) while powerP= new Polynomial(new Formula(power)) 
	 * 			which means the value of powerP is power  
	 * @throws CannotCalculateException
	 * @throws FormulaCalculationException
	 */
	@Override
	public Polynomial pow(Polynomial p,long power){
		p.removeZero();
		if(power<0){
			if(p.getNumOfFormula()==1){
				Formula f = p.getFormulas().iterator().next();
				Formula fr = Formula.ONE;
				for(int i = 0 ; i > power ; i--){
					fr=ca.divide(fr, f);
				}
				return new Polynomial(ca,fr);
			}
			throw new UnsupportedCalculationException("This Method Cannot Calculate while Power is less than 0");
		}
		if(power==0){
			if(Polynomial.ZERO.equals(p))
				throw new UnsupportedCalculationException("0^0 has not been Defined");
			else 
				return Polynomial.ONE;
		}
		Polynomial result = Polynomial.ONE;
		for(long i=0;i<power ;i++){
			result= multiply(result, p);
		}
		return result;
	}
	
	/**
	 * <b>This Method Is Not Available</b>
	 * <p>it will calculate the result of log(pd,pu), notice that it could only calculate to integer value
	 *  
	 * @param pd -down part of log
	 * @param pu -up part of log 
	 * @return log(pd,pu)
	 * @throws CannotCalculateException  if it can't calculate
	 */
	@Override
	public Polynomial log(Polynomial pd , Polynomial pu){
		pd.removeZero();
		pu.removeZero();
		if(pd.getNumOfFormula()<=pu.getNumOfFormula()){
			
		}
		throw new UnsupportedCalculationException("Can't calculate log :"+pd.toString()+"  ,  "+pu.toString());
	}
	
	/**
	 * <p>it will calculate the result of log(pd,pu), notice that it could only calculate when pu is an integer and greater than zero
	 * @param pd -down part of exp
	 * @param pu -up part of exp
	 * @return exp(pd,pu)
	 * @throws CannotCalculateException  if it can't calculate
	 */
	public Polynomial pow(Polynomial pd,Polynomial pu){
		pd.removeZero();
		int[] intValue = this.intValue(pu);
		if(intValue[0]==1){
			return this.pow(pd, intValue[1]);
		}
		throw new UnsupportedCalculationException("Can't calculate exp :"+pd.toString()+"  ,  "+pu.toString());
	}
	
	@Override
	public Polynomial abs(Polynomial p){
		p.removeZero();
		BigDecimal result = BigDecimal.ZERO;
		for(Formula f : p.getFormulas()){
			if(f.getNumOfChar()!=0){
				throw new UnsupportedCalculationException("Can't calculate abs :"+p.toString());
			}
			else{
				result = result.add(f.getNumber());
			}
			
		}
		if(result.compareTo(BigDecimal.ZERO)>0){
			return p;
		}
		else{
			return subtract(Polynomial.ZERO, p);
		}
		
	}

	
//	private static final BigInteger ANGLE_UPPER_BOUND = BigInteger.ONE;
	
	/**
	 * @param f
	 * @return
	 */
	private void reduceByTwoPi(BigInteger[] arr){
		//firstly get the Numerator
		BigInteger nume = arr[0];
		BigInteger deno = arr[1];
		deno = deno.add(deno);
		// reduce by two.
		nume = nume.mod(deno);
		arr[0] = nume;
		arr[1] = deno;
	}
	
	private void reduceByPi(BigInteger[] arr){
		// firstly get the Numerator
		BigInteger nume = arr[0];
		BigInteger deno = arr[1];
		// reduce by two.
		nume = nume.mod(deno);
		arr[0] = nume;
		arr[1] = deno;
	}
	
	private boolean reduceIntoPi(BigInteger[] nd,boolean nega){
		if(nd[0].compareTo(nd[1])>0){
			nega = !nega;
			nd[0] = nd[0].subtract(nd[1]);
		}
		return nega;
	}
	
	private boolean subtractToHalf(BigInteger[] nd,boolean nega){
		BigInteger half = nd[1].divide(BigInteger.valueOf(2));
		if(nd[0].compareTo(half)>0){
			nd[0] = nd[1].subtract(nd[0]);
			nega = !nega;
		}
		return nega;
		
	}
	
	private void addHalfPi(BigInteger[] nd){
		BigInteger[] mod = nd[1].divideAndRemainder(BigInteger.valueOf(2));
		if(mod[1].equals(BigInteger.ZERO)){
			nd[0] = nd[0].add(mod[0]);
		}else{
			nd[0] = nd[0].add(nd[0]).add(nd[1]);
			nd[1] = nd[1].add(nd[1]);
		}
	}
	
	
	/**
	 * the sin method will calculate sin(p) according to sinValue
	 * @param p -the Polynomial to calculate 
	 * @return the Polynomial that equals sin(p)
	 * @throws CannotCalculateException  if it can't calculate
	 */
	@Override
	public Polynomial sin(Polynomial p){
		p.removeZero();
		if(p.getNumOfFormula()==1){
			Formula f=p.getFormulas().iterator().next();
			Polynomial re = sinf(f);
			if(re != null){
				return re;
			}
		}
		throw new UnsupportedCalculationException("Can't calculate sin");
	}
	
	private Polynomial sinf(Formula f){
		if(f.haveSameChar(Formula.PI)){
			// ... pi
			boolean nega = f.isPositive();
			if(!nega)
				f=f.negate();
			BigInteger[] nd = new BigInteger[]{f.getNumerator(),f.getDenominator()};
			//now in [0,2Pi]
			reduceByTwoPi(nd);
			nega = reduceIntoPi(nd,nega);
			//into pi.
			//sin(x) = sin(pi-x)
			subtractToHalf(nd,true);
			
//			f = Formula.c
			Polynomial result = SIN_VALUE.get(Pair.of(nd));
			if(result != null){
				if(nega){
					result = negate(result);
				}
			}
			return result;
		}
		return null;
	}
	private Polynomial cosf(Formula f){
		if(f.haveSameChar(Formula.PI)){
			// ... pi
			boolean nega = f.isPositive();
			if(!nega)
				f=f.negate();
			BigInteger[] nd = new BigInteger[]{f.getNumerator(),f.getDenominator()};
			//cos(x) = sin(pi/2+x)
			//add 1/2 to nd
			addHalfPi(nd);
			reduceByTwoPi(nd);
			//now in [0,2Pi]
			nega = reduceIntoPi(nd,nega);
			//cos(x) = -cos(pi-x)
			nega = subtractToHalf(nd, nega);
			
			Polynomial result = SIN_VALUE.get(Pair.of(nd));//cos(x) = sin(pi/2+x),in the first we added pi/2.
			if(result != null){
				if(nega){
					result = negate(result);
				}
			}
			return result;
		}
		return null;
	}
	
	/**
	 * the cos method will calculate the cos value of p by using cos(p) = sin(Pi/2 - P)
	 * <p>
	 * @param p -the Polynomial to calculate 
	 * @return cos(p)
	 * @throws CannotCalculateException  if it can't calculate
	 */
	@Override
	public Polynomial cos(Polynomial p){	
		try{
			p.removeZero();
			if(p.getNumOfFormula()==1){
				Formula f=p.getFormulas().iterator().next();
				Polynomial re = cosf(f);
				if(re != null){
					return re;
				}
			}
		}
		catch(UnsupportedCalculationException e){
		}
		throw new UnsupportedCalculationException("Can't calculate cos");
	}
	
	/**
	 * this method will calculate the tan value of p
	 * @param p -the Polynomial to calculate 
	 * @return tan(p)
	 * @throws FormulaCalculationException  if p = Pi/2 + k Pi , k in Z
	 * @throws CannotCalculateException     if it can't calculate
	 */
	@Override
	public Polynomial tan(Polynomial p){
		p.removeZero();
		if(p.getNumOfFormula()==1){
			Formula f=p.getFormulas().iterator().next();
			
			Polynomial re = tanf(f);
			if(re!=null){
				return re;
			}
		}
		throw new UnsupportedCalculationException("Can't calculate tan : "+p.toString());
	}
	
	private Polynomial tanf(Formula f){
		if(f.haveSameChar(Formula.PI)){
			boolean nega =f.isPositive();
			if(!nega)
				f=f.negate();
			BigInteger[] nd = new BigInteger[]{f.getNumerator(),f.getDenominator()};
			reduceByPi(nd);
			//into pi,but we need in [0,1/2)pi.
			if(nd[1].equals(nd[0].multiply(BigInteger.valueOf(2l)))){
				throw new ArithmeticException("tan(Pi/2)");
			}
			nega = subtractToHalf(nd, nega);
			Polynomial result = TAN_VALUE.get(Pair.of(nd));
			if(!(result==null)){
				if(!nega)
					result = negate(result);
				return result;
			}
		}
		return null;
		
	}
	
	private Polynomial cotf(Formula f){
		if(f.haveSameChar(Formula.PI)){
			boolean nega =f.isPositive();
			if(!nega)
				f=f.negate();
			BigInteger[] nd = new BigInteger[]{f.getNumerator(),f.getDenominator()};
			addHalfPi(nd);
			reduceByPi(nd);
			nega = !nega;//cot(x) = -cot(x+Pi/2)
			//into pi,but we need in [0,1/2)pi.
			if(nd[1].equals(nd[0].multiply(BigInteger.valueOf(2l)))){
				throw new ArithmeticException("cot(0)");
			}
			nega = subtractToHalf(nd, nega);
			Polynomial result = TAN_VALUE.get(Pair.of(nd));
			if(!(result==null)){
				if(!nega)
					result = negate(result);
				return result;
			}
		}
		return null;
		
	}
	
	/**
	 * this method will calculate the tan value of p
	 * @param p -the Polynomial to calculate 
	 * @return cot(p)
	 * @throws FormulaCalculationException  if {@code p = k*Pi , k = Z}
	 * @throws CannotCalculateException     if it can't calculate
	 */
	public Polynomial cot(Polynomial p){
		try{
			p.removeZero();
			if(p.getNumOfFormula()==1){
				Formula f=p.getFormulas().iterator().next();
				Polynomial re = cotf(f);
				if(re!=null){
					return re;
				}
			}
		}catch(UnsupportedCalculationException e){
		}
		throw new UnsupportedCalculationException("Can't calculate cot :");
	}
	
	
	
	
	/**
	 * this method will calculate the arcsin value of p 
	 * @param p -the Polynomial to calculate 
	 * @return arcsin(p) whose value is larger than (or equal to) {@code -Pi/2} and less than (or equal to) {@code Pi/2}
	 * @throws CannotCalculateException     if it can't calculate
	 * @throws FormulaCalculationException  if {@code p < -1 or p > 1}
	 */
	@Override
	public Polynomial arcsin(Polynomial p){
		p.removeZero();
		Polynomial  result = ARCSIN_VALUE.get(p);
		if(result!= null)
			return result;
		result = ARCSIN_VALUE.get(negate(p));
		//try negative value
		if(result!=null)
			return negate(result);
		//this step deals with the undefined number
		if(p.getNumOfFormula()==1){
			Formula f = p.getFormulas().iterator().next();
			//f should be a constant value = [-1,1]
			if(f.getNumOfChar()==0){
				BigDecimal n = f.getNumber();
				if(n.compareTo(BigDecimal.ONE)>0||n.compareTo(new BigDecimal(-1))<0){
					throw new UnsupportedCalculationException("Arcsin undifined  :  "+p.toString());
				}
			}
		}
		throw new UnsupportedCalculationException("Cannot calculate arcsin  :  "+p.toString());
	}
	
	/**
	 * this method will calculate the arccos value of p 
	 * @param p -the Polynomial to calculate 
	 * @return arccos(p) whose value is larger than (or equal to) {@code 0} and less than (or equal to) {@code Pi}
	 * @throws CannotCalculateException     if it can't calculate
	 * @throws FormulaCalculationException  if {@code p < -1 or p > 1}
	 */
	@Override
	public Polynomial arccos(Polynomial p){
		//arccos(x) + arcsin(x) = Pi/2 --> arccos(x) = Pi/2 - arcsin(x)
		try{
			return subtract(new Polynomial(ca,Formula.valueOf("Pi/2")),arcsin(p));
		}
		catch(UnsupportedCalculationException e){
			throw new UnsupportedCalculationException("Cannot calculate arccos  :  "+p.toString());
		}
	}
	
	/**
	 * this method will calculate the arctan value of p 
	 * @param p -the Polynomial to calculate 
	 * @return arctan(p) whose value is larger than (or equal to) {@code -Pi/2} and less than (or equal to) {@code Pi/2}
	 * @throws CannotCalculateException     if it can't calculate
	 */
	@Override
	public Polynomial arctan(Polynomial p){
		p.removeZero();
		Polynomial  result = ARCTAN_VALUE.get(p);
		if(result!= null)
			return result;
		result = ARCTAN_VALUE.get(negate(p));
		//try negative value
		if(result!=null)
			return negate(result);
		throw new UnsupportedCalculationException("Cannot calculate arctan  :  "+p.toString());
	}
	
	/**
	 * this method will replace a single character in p by given Polynomial expr
	 * @param c
	 * @param p
	 * @return
	 * @throws CannotCalculateException if the power of the given char is not an int above zero
	 */
	public Polynomial replaceChar(char c,Polynomial p,Polynomial expr){
		return replace(""+c, p, expr);
	}
	/**
	 * this method will replace a single character in p by given Polynomial expr
	 * @param c
	 * @param p
	 * @return
	 * @throws CannotCalculateException if the power of the given char is not an int above zero
	 */
	public Polynomial replace(String target,Polynomial p,Polynomial expr){
		Polynomial temp,result=Polynomial.ZERO.clone();
		Formula f;
		for(Formula f1: p.getFormulas()){
			//System.out.println(f1.toString()); TODO
			if(f1.getCharacterS().containsKey(target)){
				BigDecimal power = f1.getCharacter().get(target);
					//should be an int above zero
				if(power.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO)==0){
					int t=power.intValue();
					f=f1.removeChar(target);
					temp=new Polynomial(ca,f);// a * i^2 ,i=x+y --> a* (x+y) * (x+y)
					temp = multiply(temp, pow(expr, t));
					
					result = add(result, temp);
				}
				else{
					
					throw new UnsupportedCalculationException("Cannot calculate : ");
				}
			}
			else{
				result.addFormula(f1);
			}
		}
		
		return result;
	}
	public boolean containsChar(char c,Polynomial p){
		return containsChar(""+c, p);
	}
	public boolean containsChar(String target,Polynomial p){
		for(Formula f: p.getFormulas()){
			if(f.getCharacterS().containsKey(target)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @return an integer array whose length is 2 : 
	 * the first of it is  1  if  the formula can be converted to an integer      
	 * and at this time the second of it is the integer value of the polynomial
	 * the first of it is 0 if the formula cannot be converted to an integer
	 */
	public int[] intValue(Polynomial p){
		int[] result = {0,0};
		if(p.getNumOfFormula()==1){
			Formula f =p.getFormulas().iterator().next();
			if(f.getNumOfChar()==0){
				if(f.getNumber().remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO)==0){
					result[0] = 1;
					result[1]= f.getNumber().intValueExact();
					return result;
				}
			}
		}
		return result;
	}
	@Override
	public boolean isEqual(Polynomial para1, Polynomial para2) {
		if(para1==para2)
			return true;
		return para1.equals(para2);
	}
	@Override
	public int compare(Polynomial para1, Polynomial para2) {
		if(para1==para2)
			return 0;
		return para1.compareTo(para2);
	}
	@Override
	public Polynomial getZero() {
		return Polynomial.ZERO;
	}
	@Override
	public Polynomial getOne() {
		return Polynomial.ONE;
	}
	@Override
	public Polynomial reciprocal(Polynomial p) {
		return divide(Polynomial.ONE,p);
	}
	@Override
	public Polynomial multiplyLong(Polynomial p, long l) {
		return multiply(p, 
				new Polynomial(p.getFormulas().getAdder()
						,Formula.valueOf(BigInteger.valueOf(l))));
	}
	@Override
	public Polynomial divideLong(Polynomial p, long l) {
		return divide(p, 
				new Polynomial(p.getFormulas().getAdder()
						,Formula.valueOf(BigInteger.valueOf(l))));
	}
	
	@Override
	public Polynomial squareRoot(Polynomial p) {
		//This method is a very complex one.We should first check whether this is a number
		if(p.getNumOfFormula()==1){
			//only one formula
			AdditiveSet<Formula> as = p.getFormulas();
			Formula f = as.iterator().next();
			FormulaCalculator fc = (FormulaCalculator)as.getAdder();
//			Printer.print(p);
			return new Polynomial(fc, fc.squareRoot(f));
		}
		//try some easy case ? No
		
		
		throw new UnsupportedCalculationException("Too complex");
		
		
	}
	
	
	
	@Override
	public Polynomial constantValue(String name) {
		if(name.equalsIgnoreCase(STR_PI)){
			return PI;
		}
		if(name.equalsIgnoreCase(STR_E)){
			return E;
		}
		throw new UnsupportedCalculationException("No constant value avaliable");
	}
	private static final SimplifierP sp = new SimplifierP();
	
	public static final Simplifier<Polynomial> getSimplifier(){
		return sp;
	}
	
	static class SimplifierP implements Simplifier<Polynomial>{
		
		private SimplifierP(){
		}
		
		@Override
		public List<Polynomial> simplify(List<Polynomial> numbers) {
			int len = numbers.size();
			List<Formula> list = new ArrayList<Formula>(len*2);
			int[] indexes = new int[len];
			int i = 0;
			int pos = 0;
			for(Polynomial p : numbers){
				for(Formula f : p.getFormulas()){
					list.add(f);
					i++;
				}
				indexes[pos++] = i;
			}
			List<Formula> reF = FormulaCalculator.getSimplifier().simplify(list);
			List<Polynomial> re = new ArrayList<>(len);
			i=0;
			pos = 0;
			Iterator<Formula> it = reF.iterator();
			for(Polynomial p : numbers){
				int size ;
				if(pos==0){
					size = indexes[0];
				}else{
					size = indexes[pos]-indexes[pos-1]; 
				}
				AdditiveSet<Formula> as = new AdditiveSet<>(p.getFormulas().getAdder(),size );
				for(;i<indexes[pos];i++){
					as.add(it.next());
				}
				Polynomial po = new Polynomial(as);
				re.add(po);
				pos++;
			}
			
			return re;
		}
		
		
		
	}
	
	public static void main(String[] args) {
		print("Debugging:");
		Polynomial p1 = Polynomial.valueOf("3x^2-3y^2"),
				p2 = Polynomial.valueOf("x+y");
		print(p1);
		print(p2);
		print(Polynomial.getCalculator().divide(p1, p2));
		/*
		Polynomial[] ps = new Polynomial[2];
		ps[0] = new Polynomial(DEFAULT_FORMULA_CALCULATOR, "1");
		ps[1] = new Polynomial(DEFAULT_FORMULA_CALCULATOR, "2");
		PolyCalculator pc = new PolyCalculator();
		print(pc.compare(ps[0], ps[1]));
		Formula f1 = Formula.valueOf("1");
		Formula f2 = Formula.valueOf("2");
		print(DEFAULT_FORMULA_CALCULATOR.compare(f1, f2));
		*/
	}

	@Override
	public Polynomial exp(Polynomial a, Polynomial b) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.utilities.math.MathCalculator#getNumberClass()
	 */
	@Override
	public Class<Polynomial> getNumberClass() {
		return Polynomial.class;
	}
	
	
	
}
