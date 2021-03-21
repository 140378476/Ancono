package samples;

import cn.ancono.math.algebra.abs.group.finite.PermutationGroup;
import cn.ancono.math.discrete.combination.Permutations;

/*
 * Created by lyc at 2020/3/1
 */
public class FiniteGroupSample {
    public static void permutationGroup1() {
        var G = PermutationGroup.symmetricGroup(4);
        var H = PermutationGroup.generateFrom(
                Permutations.swap(4, 0, 1),
                Permutations.swap(4, 2, 3)
        );
        var H1 = G.normalizer(H);
        System.out.println(H1.getSet());
        System.out.println(G.isNormalSubgroup(H1));
//        G.getCosets(H1,false).stream().forEach( c ->
//                System.out.println(c.getRepresentatives())
//        );
        System.out.println(G.indexOf(H1));
    }

    public static void permutationGroup2(){
        //Symmetric group A_4 is generated by p1p2, p1p3:
        var p1 = Permutations.swap(4,0,1);
        var p2 = Permutations.swap(4,2,3);
        var p3 = Permutations.swap(4,1,2);
        var G = PermutationGroup.generateFrom(
                p1.compose(p2),
                p1.compose(p3)
        );
        System.out.println(G.index());
    }


    public static void main(String[] args) {
        permutationGroup2();
    }
}
