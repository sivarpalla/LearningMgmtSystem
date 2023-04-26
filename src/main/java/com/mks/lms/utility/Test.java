package com.mks.lms.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) {
		List<Integer> l1=Arrays.asList(1,3,5,7,9,10,2);

        List<Integer> l2=Arrays.asList(1,2,3);
        
        
        //get hash set of common elements.
        Set<Integer> st= Stream.concat(l1.stream(), l2.stream()).collect(Collectors.groupingBy(i->i,Collectors.counting()))
        .entrySet().stream().filter(x->x.getValue()==1).map(x->x.getKey()).collect(Collectors.toSet());
        st.forEach(n->System.out.println(n));
        
        System.out.println("Another way");
        Stream.of(l1,l2).flatMap(list->list.stream()).collect(Collectors.groupingBy(i->i,Collectors.counting()))
        .entrySet().stream().filter(x->x.getValue()==1).map(x->x.getKey()).collect(Collectors.toSet()).forEach(n->System.out.println(n));
        

	}

}
