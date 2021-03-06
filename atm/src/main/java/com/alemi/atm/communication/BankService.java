package com.alemi.atm.communication;

import com.alemi.common.ApiPaths;
import com.alemi.common.models.auth.AuthPincode;
import com.alemi.common.models.auth.AuthRequest;
import com.alemi.common.models.transaction.BankOperation;
import com.alemi.common.models.auth.CardAuthOption;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BankService {

	@POST(ApiPaths.Bank.DEPOSIT)
	Call<BankOperation> deposit(@Body BankOperation request);

	@POST(ApiPaths.Bank.WITHDRAW)
	Call<BankOperation> withdraw(@Body BankOperation request);

	@GET(ApiPaths.Bank.CHECK_BALANCE)
	Call<BankOperation> checkBalance(@Query("cardNumber") String cardNumber);

	Call<String> login(@Body AuthRequest request);
	Call<CardAuthOption> getAuthOption(@Query("cardNumber") String cardNumber);
	Call<String> changeAuthOption(@Body AuthRequest request);
}
