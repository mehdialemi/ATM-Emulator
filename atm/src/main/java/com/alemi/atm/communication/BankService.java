package com.alemi.atm.communication;

import com.alemi.bank.card.models.CardOperationRequest;
import com.alemi.bank.card.models.CardResponse;
import com.alemi.bank.common.BankApiPaths;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BankService {

	@POST(BankApiPaths.ROOT + BankApiPaths.CARD + BankApiPaths.DEPOSIT)
	Call<CardResponse> deposit(@Body CardOperationRequest request);

	@POST(BankApiPaths.ROOT + BankApiPaths.CARD + BankApiPaths.WITHDRAW)
	Call<CardResponse> withdraw(@Body CardOperationRequest request);

	@GET(BankApiPaths.ROOT + BankApiPaths.CARD + BankApiPaths.CHECK_BALANCE)
	Call<CardResponse> checkBalance(@Query("cardNumber") String cardNumber);
}
