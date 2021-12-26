package com.alemi.atm.communication;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class BankConnector {

	@Value("${bank.service.address}")
	private String address;

	private BankService service;

	@PostConstruct
	public void init() {
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(address)
				.addConverterFactory(GsonConverterFactory.create())
				.client(httpClient.build())
				.build();
		service = retrofit.create(BankService.class);
	}

	public BankService service() {
		return service;
	}

	public <T> T execute(Call<T> call) {
		try {
			Response<T> response = call.execute();
			if (response.isSuccessful())
				return response.body();

			log.warn("Unsuccessful response from edge service, message: {}", response.message());
			throw new RuntimeException("Error in communication with exchange service");
		} catch (IOException e) {
			log.error("Error in remote call of edge service", e);
			throw new RuntimeException("Error in communication with exchange service");
		}
	}
}
