package com.alemi.common;

public class ApiPaths {

	public final static String ROOT = "/api/v1";

	public static class Atm {
		public final static String ATM_ROOT = ROOT + "/atm";

		public final static String DEPOSIT = ATM_ROOT + "/controller";
		public final static String WITHDRAW = ATM_ROOT + "/withdraw";
		public final static String CHECK_BALANCE = ATM_ROOT + "/check_balance";
	}

	public static class Bank {
		public final static String BANK_ROOT = ROOT + "/bank";

		public final static String AUTH = BANK_ROOT + "/auth";
		public final static String AUTH_GET_OPTION = AUTH + "/get/options";
		public final static String AUTH_SET_OPTION = AUTH + "/set/options";
		public final static String LOGIN = AUTH + "/login";

		public final static String CARD = BANK_ROOT + "/card";
		public final static String DEPOSIT = CARD + "/controller";
		public final static String WITHDRAW = CARD + "/withdraw";
		public final static String CHECK_BALANCE = CARD + "/check_balance";
	}
}
