/**
 * 
 */
package cn.timelives.java.math.planeAG;

import java.util.Objects;
import java.util.function.Function;

import cn.timelives.java.math.FlexibleMathObject;
import cn.timelives.java.math.function.MathFunction;
import cn.timelives.java.math.numberModels.MathCalculator;
import cn.timelives.java.math.numberModels.NumberFormatter;

/**
 * Plane affine transformation is the union of all the basic linear transformations in a plane. It contains translation, rotation, flip and some other transformations.
 * This class provides method {@link #apply(Point)} to apply this transformation as a {@link MathFunction} of type Point<T>. This class is also the basic of the TransformedCurve.
 * <p>
 * The general affine transformation can be represented by the following expression:
 * <pre>x' = a1*x + b1*y + c1
 *y' = a2*x + b2*y + c2</pre>
 * 
 * @author liyicheng
 *
 */
public final class PAffineTrans<T> extends FlexibleMathObject<T> implements PointTrans<T> {
	/*
	 * Describes the transformation matrix as:
	 * a1 b1
	 * a2 b2
	 */
	private final TransMatrix<T> mat;
	private final PVector<T> v;
	
	
	/**
	 * @param mc
	 */
	protected PAffineTrans(MathCalculator<T> mc,TransMatrix<T> mat,PVector<T> v) {
		super(mc);
		this.mat = Objects.requireNonNull(mat);
		this.v = Objects.requireNonNull(v);
	}

	/** 
	 * Performs the transformation, returns the result as a point.
	 * @param p a point
	 * @return the point after translation
	 */
	@Override
	public Point<T> apply(Point<T> p) {
		return mat.transform(p).translate(v);
	}
	/**
	 * Performs the transformation with the coordinate x and y.
	 * @param x coordinate x
	 * @param y coordinate y
	 * @return a point
	 */
	public Point<T> apply(T x,T y){
		return apply(Point.valueOf(x, y, mc));
	}
	/**
	 * Gets the TransMatrix of this affine transformation, the matrix will be 
	 * <pre>
	 * (a1 b1)
	 * (a2 b2)
	 * </pre>
	 * 
	 * @return the TransMatrix of this transformation
	 */
	public TransMatrix<T> getMatrix(){
		return mat;
	}
	/**
	 * Gets the translation vector of this affine transformation, the vector will be 
	 * {@literal (c1,c2)}
	 * 
	 * @return the translation vector of this transformation
	 */
	public PVector<T> getVector(){
		return v;
	}
	
	/**
	 * Returns a composed transformation that performs this transformation first and then performs a translate.
	 * @param vec the vector
	 * @return a new PAffineTrans
	 */
	public PAffineTrans<T> translate(PVector<T> vec){
		return new PAffineTrans<>(mc, mat, v.add(vec));
	}
	/**
	 * Returns a composed transformation that performs this transformation first and then performs a center affine transformation 
	 * described by a TransformMatrix.
	 * @param tm a TransMatrix
	 * @return a new PAffineTrans
	 */
	public PAffineTrans<T> translate(TransMatrix<T> tm){
		TransMatrix<T> nmat = mat.andThen(tm);
		PVector<T> nv = tm.transform(v);
		return new PAffineTrans<>(mc, nmat, nv);
	}
	/**
	 * Returns a composed transformation that performs this transformation first and then performs a rotate operation, with its angle(anticlockwise) given.
	 * @param angle the angle to rotate
	 * @return a new PAffineTrans
	 */
	public PAffineTrans<T> rotate(T angle){
		//use the corresponding method in TransMatrix
		return translate(TransMatrix.rotate(angle, mc));
	}
	/**
	 * Returns a composed transformation that apply this first and then apply the {@code after} transformation. The new transformation will be 
	 * a PAffineTrans.
	 * @param pat a PAffineTrans
	 * @return a composed transformation
	 */
	public PAffineTrans<T> andThen(PAffineTrans<T> after){
		TransMatrix<T> nmat = mat.andThen(after.mat);
		PVector<T> nv = after.mat.transform(v).add(after.v);
		return new PAffineTrans<>(mc, nmat, nv);
	}
	
	
	
	private PAffineTrans<T> inversed = null;
	/**
	 * Returns the inverse of this, throws an {@link ArithmeticException} if this operation cannot be done.
	 * @return the inverse of this affine transformation
	 * @throws ArithmeticException if this method failed
	 */
	public PAffineTrans<T> inverse(){
		if(inversed == null){
			TransMatrix<T> matInversed ;
			try{
				matInversed = mat.inverse();
			}catch(ArithmeticException ae){
				throw new ArithmeticException("Cannot inverse transformation.");
			}
			T nc1 = mc.negate(mc.add(mc.multiply(matInversed.getNumber(0, 0), v.x), 
					mc.multiply(matInversed.getNumber(0, 1), v.y)));
			T nc2 = mc.negate(mc.add(mc.multiply(matInversed.getNumber(1, 0), v.x), 
					mc.multiply(matInversed.getNumber(1, 1), v.y)));
			PVector<T> nv = PVector.valueOf(nc1, nc2, mc);
			inversed = new PAffineTrans<>(mc, matInversed, nv);
			inversed.inversed = this;
		}
		return inversed;
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#mapTo(java.util.function.Function, cn.timelives.java.math.number_models.MathCalculator)
	 */
	@Override
	public <N> PAffineTrans<N> mapTo(Function<T, N> mapper, MathCalculator<N> newCalculator) {
		return new PAffineTrans<>(newCalculator, mat.mapTo(mapper, newCalculator), v.mapTo(mapper, newCalculator));
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this==obj){
			return true;
		}
		if(!(obj instanceof PAffineTrans)){
			return false;
		}
		PAffineTrans<?> pat = (PAffineTrans<?>) obj;
		return mat.equals(pat.mat) && v.equals(pat.v);
	}
	
	private int hash;
	
	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#hashCode()
	 */
	@Override
	public int hashCode() {
		if(hash == 0){
			int ha = mat.hashCode();
			ha  = ha*31 + v.hashCode();
			hash = ha;
		}
		return hash;
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#valueEquals(cn.timelives.java.math.FlexibleMathObject)
	 */
	@Override
	public boolean valueEquals(FlexibleMathObject<T> obj) {
		if(this==obj){
			return true;
		}
		if(!(obj instanceof PAffineTrans)){
			return false;
		}
		PAffineTrans<T> pat = (PAffineTrans<T>) obj;
		return mat.valueEquals(pat.mat) && v.valueEquals(pat.v);
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#valueEquals(cn.timelives.java.math.FlexibleMathObject, java.util.function.Function)
	 */
	@Override
	public <N> boolean valueEquals(FlexibleMathObject<N> obj, Function<N, T> mapper) {
		if(!(obj instanceof PAffineTrans)){
			return false;
		}
		PAffineTrans<N> pat = (PAffineTrans<N>) obj;
		return mat.valueEquals(pat.mat,mapper) && v.valueEquals(pat.v,mapper);
	}

	/* (non-Javadoc)
	 * @see cn.timelives.java.math.FlexibleMathObject#toString(cn.timelives.java.math.number_models.NumberFormatter)
	 */
	@Override
	public String toString(NumberFormatter<T> nf) {
		StringBuilder sb = new StringBuilder();
		sb.append("Affine Transformation: x'=");
		append(sb,nf,0);
		sb.append(" , y'=");
		append(sb,nf,1);
		
		return sb.toString();
	}
	private void append(StringBuilder sb,NumberFormatter<T> nf,int n){
		boolean appended = false;
		if(!mc.isZero(mat.getNumber(n, 0))){
			appended = true;
			sb.append("(").append(nf.format(mat.getNumber(n, 0), mc)).append(")x+");
		}
		if(!mc.isZero(mat.getNumber(n, 1))){
			appended = true;
			sb.append("(").append(nf.format(mat.getNumber(n, 1), mc)).append(")y+");
		}
		if(!appended || !mc.isZero(v.getNumber(n))){
			sb.append(nf.format(v.getNumber(n),mc));
		}else{
			sb.deleteCharAt(sb.length()-1);
		}
	}
	/**
	 * Returns a identity affine transformation, which keeps all the points unmoved.
	 * <pre>x' = x \n
	 *y'=y
	 * @param mc
	 * @return a new PAffineTrans
	 */
	public static <T> PAffineTrans<T> identity(MathCalculator<T> mc){
		return new PAffineTrans<>(mc, TransMatrix.identityTrans(mc), PVector.zeroVector(mc));
	}
	/**
	 * Returns a transformation that translates points.
	 * <br>
	 * @param v a vector
	 * @return a new PAffineTrans
	 */
	public static <T> PAffineTrans<T> ofTranslation(PVector<T> v){
		MathCalculator<T> mc = v.getMathCalculator();
		return new PAffineTrans<>(mc, TransMatrix.identityTrans(mc), v);
	}
	
	/**
	 * Returns a transformation that performs as the TransMatrix
	 * <br>
	 * @param m a TransMatrix
	 * @return a new PAffineTrans
	 */
	public static <T> PAffineTrans<T> ofTransMatrix(TransMatrix<T> m){
		MathCalculator<T> mc = m.getMathCalculator();
		return new PAffineTrans<>(mc, m, PVector.zeroVector(mc));
	}
	/**
	 *  Returns a transformation that performs TransMatrix first, and then performs the translate, 
	 *  which is the same as {@code fromTransMatrix(m).translate(v)}.
	 * @param m a TransMatrix
	 * @param v a PVector
	 * @return a new PAffineTrans
	 */
	public static <T> PAffineTrans<T> valueOf(TransMatrix<T> m,PVector<T> v){
		MathCalculator<T> mc = m.getMathCalculator();
		return new PAffineTrans<>(mc, m, v);
	}
	
}
