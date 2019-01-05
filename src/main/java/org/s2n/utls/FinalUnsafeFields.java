import java.util.*;
import java.lang.reflect.*;
import  sun.misc.Unsafe;
public class FinalUnsafeFields {
    private static final Unsafe unsafe;
    static
    {
        try
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws NoSuchFieldException {
        //updateFinal();
        updateFinalStatic();
    }


	private static void updateFinalStatic() throws NoSuchFieldException {
       // System.out.println( "Original static final value = " + SafeKlass.getNotSoConstant() );
        //we need a field to update
        final Field fieldToUpdate = SafeKlass.class.getDeclaredField( "NOT_SO_CONSTANT" );
        //this is a 'base'. Usually a Class object will be returned here.
        final Object base = unsafe.staticFieldBase( fieldToUpdate );
        //this is an 'offset'
        final long offset = unsafe.staticFieldOffset( fieldToUpdate );
        //actual update
        unsafe.putObject( base, offset, 145 );
        //ensure the value was updated
        System.out.println( "Updated static final value = " + SafeKlass.getNotSoConstant() );
    }

    private static void updateFinalStatic2() throws NoSuchFieldException {
        //System.out.println( "Original static final value = " + SafeKlass.getNotSoConstant() );
        //we need a field to update
        HashMap m = new HashMap(3, .13f);
        final Field fieldToUpdate = HashMap.class.getDeclaredField( "loadFactor" );
        fieldToUpdate.setAccessible(true);
      	//fieldToUpdate.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        //this is a 'base'. Usually a Class object will be returned here.
         Field modifiersField = Field.class.getDeclaredField( "modifiers" );
		            modifiersField.setAccessible( true );
		            modifiersField.setInt( field, field.getModifiers() & ~Modifier.FINAL );

        final Object base = unsafe.staticFieldBase( fieldToUpdate );
        //this is an 'offset'
        final long offset = unsafe.staticFieldOffset( fieldToUpdate );
        //actual update
        unsafe.putObject( base, offset, 145 );
        //ensure the value was updated
        System.out.println( "Updated static final value = " + m.loadFactor);
    }

    private static void updateFinal() throws NoSuchFieldException {
	        final JustFinal obj = new JustFinal( 10 );
	        //ensure we can see an original value
	        System.out.println( "Original value = " + obj.getField() );
	        //obtain an updated field
	        final Field fieldToUpdate = JustFinal.class.getDeclaredField( "m_field" );
	        //get unsafe offset to this field
	        final long offset = unsafe.objectFieldOffset( fieldToUpdate );
	        //actual update
	        unsafe.putInt( obj, offset, 20 );
	        //ensure update was successful
	        System.out.println( "Updated value = " + obj.getField() );
	    }

}