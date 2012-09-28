package infrastructure;

public abstract class Func<TParam1, TParam2, TReturn> 
{
	public abstract TReturn execute(TParam1 param1, TParam2 param2);
}
