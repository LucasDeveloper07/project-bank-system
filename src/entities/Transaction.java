package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import enums.TransactionType;

public class Transaction {

    private Integer id;
    private TransactionType transactionType;
    private Double value;
    private LocalDateTime date;

    private Account originAccount;
    private Account destinationAccount;

    public Transaction(TransactionType transactionType, Double value, LocalDateTime date, Account originAccount, Account destinationAccount) {
        this.transactionType = transactionType;
        this.value = value;
        this.date = date;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Account getOriginAccount() {
        return originAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public String viewProof() {
        StringBuilder sb = new StringBuilder();

        if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.WITHDRAW) {
            sb.append("=====COMPROVANTE DA TRANSAÇÃO=====\n");
            sb.append("Id: " + id);
            sb.append("\nTransação: " + transactionType);
            sb.append("\nValor: R$" + String.format("%.2f", value));
            sb.append("\nData: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    
            return sb.toString();
        } else if (transactionType == TransactionType.TRANSFER) {
            sb.append("=====COMPROVANTE DA TRANSAÇÃO=====\n");
            sb.append("Id: " + id);
            sb.append("\nTransação: " + transactionType);
            sb.append("\nConta origem: " + originAccount.getUser().getName());
    
            User verificationOrigin = originAccount.getUser();
    
            if (verificationOrigin instanceof ClientPf clientPf) {
                sb.append(" - " + clientPf.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"));
            } else if (verificationOrigin instanceof ClientPj clientPj) {
                sb.append(" - " + clientPj.getCnpj().replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"));
            }
    
            sb.append("\nConta destino: " + destinationAccount.getUser().getName());
    
            User verificationDestination = destinationAccount.getUser();
    
            if (verificationDestination instanceof ClientPf clientPf) {
                sb.append(" - " + clientPf.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"));
            } else if (verificationDestination instanceof ClientPj clientPj) {
                sb.append(" - " + clientPj.getCnpj().replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"));
            }
    
            sb.append("\nValor: R$" + String.format("%.2f", value));
            sb.append("\nData: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    
            return sb.toString();
        } else if (transactionType == TransactionType.MAINTENANCE_FEE) {
            sb.append("=====COMPROVANTE DA TRANSAÇÃO=====\n");
            sb.append("Id: " + id);
            sb.append("\nTransação: " + transactionType);
            sb.append("\nValor: R$" + String.format("%.2f", value));
            sb.append("\nData: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    
            return sb.toString();
        } else {
            sb.append("=====COMPROVANTE DA TRANSAÇÃO=====\n");
            sb.append("Id: " + id);
            sb.append("\nTransação: " + transactionType);
            sb.append("\nValor: R$" + String.format("%.2f", value));
            sb.append("\nData: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    
            return sb.toString();
        }
        
    }
}
