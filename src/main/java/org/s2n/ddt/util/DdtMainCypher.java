package org.s2n.ddt.util;

public class DdtMainCypher {

	private static final String key = "fgjkljrt4ttgk904054kgldKER439403";// 32

	private static CipherUtil mainCrpt;
	static {
		try {
			mainCrpt = new CipherUtil(key);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String enc(String s) throws Exception {
		return mainCrpt.encrypt(s);

	}

	public static String dec(String s) throws Exception {
		return mainCrpt.decrypt(s);

	}
	
	public static void main(String[] args) {
		
		try {
			System.out.println(dec("qUJsg2ttYVWq27O6IeDFDzRawAjgzVCmJJESKxMO9llpGgCCNubD7HgrRHQuL/Ng"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(args != null && args.length == 1){
			String s = args[0];
			try {
				System.out.println(enc(s));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Could not encryptd :" + s+ "]");
			}
		}
	}

}
