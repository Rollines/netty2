package com.cjl.netty.data.algorithms;

/**
 * @author chenjunlin
 * @date 2018-12-27
 */
public class DataAlgorithms {
    //直接插入排序
    public static void insertSort(int[] a){
        if(a == null || a.length == 0) {//判断数组是否为空
            System.out.println("该数组为空！");
            return;
        }
        int n = a.length;//将数组的长度赋给n是为了防止每次for循环中判断时都调用length方法影响性能
        int temp;//放于for循环外面是为了防止重复创建变量
        int j;
        for(int i = 1; i < n;i++){//排序的趟数
            temp = a[i];//赋给temp是为了防止索引i之前的元素向后移动覆盖了索引i的元素
            j = i-1;
            for(; j>=0&&a[j]>temp; --j) {//将大于i位置元素的元素向后移
                a[j+1] = a[j];
            }
            a[j+1]= temp;//找到i应该在的位置，将值放置此处
        }
    }
   /*
   * 希尔排序
   *
   * 将数的个数设为n，取奇数k=n/2，将下标差值为k的数分为一组，构成有序序列。
   * 再取k=k/2 ，将下标差值为k的书分为一组，构成有序序列。
   * 重复第二步，直到k=1执行简单插入排序。
   */
    public void sheelSort(int [] a){
        int len=a.length;//单独把数组长度拿出来，提高效率
        while(len!=0){
            len=len/2;
            for(int i=0;i<len;i++){//分组
                for(int j=i+len;j<a.length;j+=len){//元素从第二个开始
                    int k=j-len;//k为有序序列最后一位的位数
                    int temp=a[j];//要插入的元素
                    /*for(;k>=0&&temp<a[k];k-=len){
                        a[k+len]=a[k];
                    }*/
                    while(k>=0&&temp<a[k]){//从后往前遍历
                        a[k+len]=a[k];
                        k-=len;//向后移动len位
                    }
                    a[k+len]=temp;
                }
            }
        }
    }
    public static void main(String[] args) {
        int[] a = { 37, 47, 23, 100, 19, 56, 56, 99, 9 };
        insertSort(a);
        System.out.println(a);
        for(int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
