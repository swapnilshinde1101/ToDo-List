
@FunctionalInterface
interface Inter{
	public void show(int a,int b);
	default void show1()
	{
		
	}
}

interface Inter1 extends Inter{
	public void show1(int a,int b);
	
}
public class TestFeature implements Inter{
	
	public void show(int a,int b)
	{
	     System.out.println("Hello");
	}
	
	

	public static void main(String[] args) {
		new TestFeature().show(10,20);

	}

}
