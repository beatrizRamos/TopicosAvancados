package src.br.ifpe.service;

import java.util.List;

import src.br.ifpe.entities.Account;
import src.br.ifpe.entities.Client;

public interface BankService {
	public List<Account> listAccounts();
	public List<Client> listClients();
}
