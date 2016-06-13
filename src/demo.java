class A
{
     String m=test();
    public  String test()
    {
        System.out.println("m="+m);
        return "abc";
    }
    static
    {
        System.out.println("A");
    }
    {
        System.out.println("B");
    }
    public A()
    {
        System.out.println("C");
    }
}
class demo extends A
{
    static
    {
        System.out.println("D");
    }
    {
        System.out.println("E");
    }
    public demo()
    {
        System.out.println("F");
    }
    public static void main(String[] args)
    {
        System.out.println("G");
        new demo();
        new demo();
        System.out.println("H");
    }
}