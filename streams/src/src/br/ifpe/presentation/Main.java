package src.br.ifpe.presentation;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import src.br.ifpe.entities.Account;
import src.br.ifpe.entities.AccountEnum;
import src.br.ifpe.entities.Client;
import src.br.ifpe.service.BankService;
import src.br.ifpe.service.ServiceFactory;

/**
 * OBSERVAÇÕES: NÃO é permitido o uso de nenhuma estrutura de repetição (for,
 * while, do-while). Tente, ao máximo, evitar o uso das estruturas if, else if,
 * else e switch-case
 *
 * @author Victor Lira
 *
 */
public class Main {

    private static BankService service = ServiceFactory.getService();

    public static void main(String[] args) {
       imprimirNomesClientes();
        //TODO to test here
    }

    /**
     * 1. Imprima na tela o nome e e-mail de todos os clientes (sem repetição),
     * usando o seguinte formato: Victor Lira - vl@cin.ufpe.br
     */
    public static void imprimirNomesClientes() {
        service
                .listClients()
                .stream()
                .map(cliente -> cliente.getName() + " - " + cliente.getEmail())
                .distinct()
                .forEach(System.out::println);

    }

    /**
     * 2. Imprima na tela o nome do cliente e a média do saldo de suas contas,
     * ex: Victor Lira - 352
     */
    public static void imprimirMediaSaldos() {
        service
                .listClients()
                .stream()
                .forEach(client -> {
                    double media
                            = service.listAccounts()
                                    .stream()
                                    .filter(account -> account.getClient().getName().equals(client.getName()))
                                    .mapToDouble(account -> account.getBalance())
                                    .average().getAsDouble();
                    System.out.println(client.getName() + " - " + media);
                });
        /*
        service
                .listClients()
                .stream()
                .map(client -> client.getName() + " - " + service.listAccounts()
                .stream()
                .filter(account -> account.getClient().getName().equals(client.getName()))
                .mapToDouble(account -> account.getBalance())
                .average().getAsDouble()
                )
                .forEach(System.out::println);*/

    }

    /**
     * 3. Considerando que só existem os países "Brazil" e "United States",
     * imprima na tela qual deles possui o cliente mais rico, ou seja, com o
     * maior saldo somando todas as suas contas.
     */
    public static void imprimirPaisClienteMaisRico() {


        service
                .listClients()
                .stream()
                .forEach((Client client) -> {
                    double sumBrazil = 0.0;
                    double sumAccounts = service.listAccounts()
                            .stream()
                            .filter(account -> account.getClient().getAddress().getCountry().equals("Brazil"))
                            .filter(account -> account.getClient().getName().equals(client.getName()))
                            .collect(Collectors.summarizingDouble(Account::getBalance)).getSum();
                    sumBrazil = Double.compare(sumAccounts, sumBrazil);

                    double sumEUA = 0.0;
                    double sumAccountsEUA = service.listAccounts()
                            .stream()
                            .filter(account -> account.getClient().getAddress().getCountry().equals("United States"))
                            .filter(account -> account.getClient().getName().equals(client.getName()))
                            .collect(Collectors.summarizingDouble(Account::getBalance)).getSum();
                    sumEUA = Double.compare(sumAccountsEUA, sumEUA);
                    if(sumBrazil > sumEUA){
                        System.out.println("Brazil");
                    }
                    
                    else{
                        System.out.println("United States");
                    }
                    
                });

    }

    /**
     * 4. Imprime na tela o saldo médio das contas da agência
     *
     * @param agency
     */
    public static void imprimirSaldoMedio(int agency) {
        double saldoMedio = service
                .listAccounts()
                .stream()
                .filter(account -> account.getAgency() == agency)
                .mapToDouble(account -> account.getBalance())
                .average().getAsDouble();
        System.out.println(saldoMedio);

    }

    /**
     * 5. Imprime na tela o nome de todos os clientes que possuem conta poupança
     * (tipo SAVING)
     */
    public static void imprimirClientesComPoupanca() {
        List<String> names = service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getType() == AccountEnum.SAVING)
                .map(accounts -> accounts.getClient().getName())
                .distinct()
                .collect(Collectors.toList());
        names.forEach(System.out::println);
    }

    /**
     * 6.
     *
     * @param agency
     * @return Retorna uma lista de Strings com o "estado" de todos os clientes
     * da agência
     */
    public static List<String> getEstadoClientes(int agency) {
        return service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getAgency() == agency)
                .map(accounts -> accounts.getClient().getAddress().getState())
                .collect(Collectors.toList());
    }

    /**
     * 7.
     *
     * @param country
     * @return Retorna uma lista de inteiros com os números das contas daquele
     * país
     */
    public static int[] getNumerosContas(String country) {
        int[] numbersAccounts = service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getClient().getAddress().getCountry().equals(country))
                .mapToInt(accounts -> accounts.getNumber())
                .toArray();
        return numbersAccounts;

        
    }

    /**
     * 8. Retorna o somatório dos saldos das contas do cliente em questão
     *
     * @param clientEmail
     * @return
     */
    public static double getMaiorSaldo(String clientEmail) {
        double sumBalance = service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getClient().getEmail().equals(clientEmail))
                .mapToDouble(accounts -> accounts.getBalance())
                .sum();
        return sumBalance;
    }

    /**
     * 9. Realiza uma operação de saque na conta de acordo com os parâmetros
     * recebidos
     *
     * @param agency
     * @param number
     * @param value
     */
    public static void sacar(int agency, int number, double value) {
        service
                .listAccounts()
                .stream()
                .filter(account -> account.getAgency() == agency && account.getNumber() == number)
                .map(account -> account.getBalance() - value);
    }

    /**
     * 10. Realiza um deposito para todos os clientes do país em questão
     *
     * @param country
     * @param value
     */
    public static void depositar(String country, double value) {
        service
                .listAccounts()
                .stream()
                .filter(account -> account.getClient().getAddress().getCountry().equals(country))
                .map(account -> account.getBalance() + value)
                .collect(Collectors.toList());
        //.forEach(System.out::println);
    }

    /**
     * 11. Realiza uma transferência entre duas contas de uma agência.
     *
     * @param agency - agência das duas contas
     * @param numberSource - conta a ser debitado o dinheiro
     * @param numberTarget - conta a ser creditado o dinheiro
     * @param value - valor da transferência
     */
    public static void transferir(int agency, int numberSource, int numberTarget, double value) {
        service
                .listAccounts()
                .stream()
                .filter(account -> account.getAgency() == agency && account.getNumber() == numberSource)
                .map(account -> account.getBalance() - value);
        service
                .listAccounts()
                .stream()
                .filter(account -> account.getAgency() == agency && account.getNumber() == numberTarget)
                .map(account -> account.getBalance() + value);

    }

    /**
     * 12.
     *
     * @param clients
     * @return Retorna uma lista com todas as contas conjuntas (JOINT) dos
     * clientes
     */
    public static List<Account> getContasConjuntas(List<Client> clients) {
        List<Account> accounts = service.listAccounts()
                .stream()
                .filter(account -> account.getType() == AccountEnum.JOINT)
                .collect(Collectors.toList());
        return accounts;

    }

    /**
     * 13.
     *
     * @param state
     * @return Retorna uma lista com o somatório dos saldos de todas as contas
     * do estado
     */
    public static double getSomaContasEstado(String state) {
        double sumBalanceState = service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getClient().getAddress().getState().equals(state))
                .mapToDouble(accounts -> accounts.getBalance())
                .sum();
        return sumBalanceState;
    }

    /**
     * 14.
     *
     * @return Retorna um array com os e-mails de todos os clientes que possuem
     * contas conjuntas
     */
    public static String[] getEmailsClientesContasConjuntas() {
        List<String> emails = service.listAccounts()
                .stream()
                .filter(accounts -> accounts.getType() == AccountEnum.JOINT)
                .map(accounts -> accounts.getClient().getEmail())
                .distinct()
                .collect(Collectors.toList());

        return (String[]) emails.toArray();
    }

    /**
     * 15.
     *
     * @param number
     * @return Retorna se o número é primo ou não
     */
    public static boolean isPrimo(int number) {
        return IntStream.rangeClosed(2, number / 2).noneMatch(i -> number % i == 0);
    }

    /**
     * 16.
     *
     * @param number
     * @return Retorna o fatorial do número
     */
    public static int getFatorial(int number) {
        return IntStream.rangeClosed(2, number).reduce(1, (x, y) -> x * y);

    }
}
