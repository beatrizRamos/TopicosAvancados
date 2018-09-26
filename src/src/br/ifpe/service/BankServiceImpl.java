package src.br.ifpe.service;

import java.util.List;

import src.br.ifpe.entities.Account;
import src.br.ifpe.entities.Client;
import src.br.ifpe.helper.LoadEntities;

public class BankServiceImpl implements BankService {

	protected BankServiceImpl() { }
	
	@Override
	public List<Account> listAccounts() {
		return LoadEntities.ACCOUNTS;
	}

	@Override
	public List<Client> listClients() {
		return LoadEntities.CLIENTS;
	}
	
}
