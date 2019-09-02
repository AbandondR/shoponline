package com.shop.online.common;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
public class StreamTest {

    @Data
    static class A {
        private String id;
        private List<B> bList;

       public A(String id, List<B> bList) {
           this.id = id;
           this.bList = bList;
       }
   }
   @Data
    static class B{
        private String id;
        public B(String id) {
            this.id = id;
        }
    }

//    public static void main(String[] args) {
//       List<A> aList = new ArrayList<>();
//       List<B> bList1 = new ArrayList<>();
//
//        bList1.add(new B("1"));
//        bList1.add(new B("2"));
//        bList1.add(new B("3"));
//        bList1.add(new B("4"));
//        List<B> bLis2 = new ArrayList<>();
//        bLis2.add(new B("5"));
//        bLis2.add(new B("6"));
//        bLis2.add(new B("7"));
//        bLis2.add(new B("8"));
//
//        aList.add(new A("1", bList1));
//        aList.add(new A("2", bLis2));
//
//        Map<String, List<String>> map = new HashMap<>();
//        List<String> includeList1 = new ArrayList<>();
//        includeList1.add("2");
//        includeList1.add("3");
//
//        List<String> includeList2 = new ArrayList<>();
//        includeList2.add("5");
//        includeList2.add("6");
//        map.put("1", includeList1);
//        map.put("2", includeList2);
//        /*Iterator<A> iterator = aList.iterator();
//        while (iterator.hasNext()) {
//            List<B> bList = iterator.next().getBList();
//            bList.stream().filter(e->map.get())
//        }*/
//        List<A> news = aList.stream().filter(e->{
//            /*Iterator<B> iterator2 = e.getBList().iterator();
//            while(iterator2.hasNext()) {
//                if(map.get(e.getId()).contains(iterator2.next().getId())) {
//                    return true;
//                }
//            }*/
//            List<B> bTest = e.getBList().stream().filter(b->
//                map.get(e.getId()).contains(b.getId())
//            ).collect(Collectors.toList());
//            e.setBList(bTest);
//            return true;
//        }).collect(Collectors.toList());
//
//        System.out.println("");
//    }
    public static void main(String[] args) {
        int i = 0;

        do{
            System.out.println(i++);

        }while(test1());
    }
    public static boolean test1() {
        return false;
    }
    public static void Demo1() {
        out: for(int i = 1; i< 5; i++){
            System.out.print("i="+i+" ");

            in: for(int j = 1; j < 5; j++){
                System.out.print("j="+j + " ");
                break in;//跳出in，in代表内循环，所以每次的j都等于1
            }
        }
    }

    public static void Demo2() {
        out: for(int i = 1; i< 5; i++){
            System.out.print("i="+i+" ");

            in: for(int j = 1; j < 5; j++){
                System.out.print("j="+j + " ");
                break out;//跳出out，out代表外循环，所以只打印一轮
            }
        }
    }
}

