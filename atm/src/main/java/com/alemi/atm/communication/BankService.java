package com.alemi.atm.communication;

import com.alemi.bank.card.models.CardOperationRequest;
import com.alemi.bank.card.models.CardResponse;
import com.alemi.bank.common.BankApiPaths;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BankService {

	@POST(BankApiPaths.ROOT + BankApiPaths.CARD + BankApiPaths.DEPOSIT)
	Call<CardResponse> deposit(@Body CardOperationRequest request);
}
