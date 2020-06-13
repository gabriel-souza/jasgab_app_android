package br.com.jasgab.jasgab.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.CustomerData;
import br.com.jasgab.jasgab.model.ResponseCustomer;

public class CustomerDAO {
    private SQLiteDatabase db;

    static final String bills_table = "bills";
    static final String connections_table = "connections";
    static final String contracts_table = "contracts";
    static final String customers_table = "customers";
    static final String id = "id";

    //Customer
    static final String address = "address";
    static final String cpf = "cpf";
    static final String email = "email";
    static final String fone = "fone";
    static final String name = "name";
    static final String neighborhood = "neighborhood";
    static final String status = "status";
    static final String zipCode = "zipCode";

    //Bill
    static final String amount = "amount";
    static final String barcode = "barcode";
    static final String bill_name = "bill_name";
    static final String expiration_date = "expiration_date";
    static final String path_pdf = "path_pdf";

    //Connection
    static final String contract_id = "contract_id";
    static final String blocked = "blocked";
    static final String blocking_reason = "blocking_reason";
    static final String customer_since = "customer_since";
    static final String latitude = "latitude";
    static final String longitude = "longitude";
    static final String mac_address = "mac_address";
    static final String username = "username";

    //Contract
    static final String adherence_since = "adherence_since";
    static final String plan_ = "plan_";

    public CustomerDAO(Context context){
        JasgabDB jasgabDB = new JasgabDB(context);
        db = jasgabDB.getWritableDatabase();
    }

    public static CustomerDAO start(Context context){
        return new CustomerDAO(context);
    }

    public ResponseCustomer select() {
        Customer customer = selectCustomer();
        if(customer == null){
            return null;
        }
        CustomerData customerData = new CustomerData();
        customerData.setBills(selectBills());
        customerData.setConnections(selectConnections());
        customerData.setContracts(selectContracts());

        ResponseCustomer responseCustomer = new ResponseCustomer();
        responseCustomer.setCustomer(customer);
        responseCustomer.setCustomerData(customerData);

        return responseCustomer;
    }

    public void inserir(ResponseCustomer responseCustomer) {
        delete();
        insertCustomer(responseCustomer.getCustomer());

        CustomerData customerData = responseCustomer.getCustomerData();
        insertBills(customerData.getBills());
        insertConnections(customerData.getConnections());
        insertContracts(customerData.getContracts());
    }

    public Customer selectCustomer(){
        Cursor cursor = db.query(customers_table, new String[] {
                        id,
                        address,
                        cpf,
                        email,
                        fone,
                        name,
                        neighborhood,
                        status,
                        zipCode},
                null,null,null,null,null, null);

        Customer customer = null;

        if(cursor.moveToLast()) {
            customer = new Customer();
            customer.setId(cursor.getInt(0));
            customer.setAddress(cursor.getString(1));
            customer.setCpf(cursor.getString(2));
            customer.setEmail(cursor.getString(3));
            customer.setFone(cursor.getString(4));
            customer.setName(cursor.getString(5));
            customer.setNeighborhood(cursor.getString(6));
            customer.setStatus(cursor.getInt(7) == 1);
            customer.setZipCode(cursor.getString(8));
        }

        return customer;
    }

    public List<Bill> selectBills(){
        @SuppressLint("Recycle") Cursor cursor = db.query(bills_table, new String[] {
                        id,
                        amount,
                        barcode,
                        bill_name,
                        expiration_date,
                        path_pdf
                },
                null,null,null,null,null, null);
        List<Bill> bills = new ArrayList<>();

        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setAmount(cursor.getDouble(1));
            bill.setBarcode(cursor.getString(2));
            bill.setBillName(cursor.getString(3));
            bill.setExpirationDate(cursor.getString(4));
            bill.setPathPdf(cursor.getString(5));
            bills.add(bill);
        }

        return bills;
    }

    public List<Connection> selectConnections(){

        Cursor cursor = db.query(connections_table, new String[] {
                        id,
                        address,
                        contract_id,
                        blocked,
                        blocking_reason,
                        customer_since,
                        latitude,
                        longitude,
                        mac_address,
                        username
                },
                null,null,null,null,null, null);
        List<Connection> connections = new ArrayList<>();

        while (cursor.moveToNext()) {
            Connection connection = new Connection();
            connection.setId(cursor.getInt(0));
            connection.setAddress(cursor.getString(1));
            connection.setContractId(cursor.getInt(2));
            connection.setBlocked(cursor.getInt(3) == 1);
            connection.setBlockingReason(cursor.getString(4));
            connection.setCustomerSince(cursor.getString(5));
            connection.setLatitude(cursor.getString(6));
            connection.setLongitude(cursor.getString(7));
            connection.setMacAddress(cursor.getString(8));
            connection.setUsername(cursor.getString(9));
            connections.add(connection);
        }

        return connections;
    }

    public List<Contract> selectContracts(){

        Cursor cursor = db.query(contracts_table, new String[] {
                        id,
                        adherence_since,
                        expiration_date,
                        plan_
                },
                null,null,null,null,null, null);
        List<Contract> contracts = new ArrayList<>();

        while (cursor.moveToNext()) {
            Contract contract = new Contract();
            contract.setId(cursor.getInt(0));
            contract.setAdherenceSince(cursor.getString(1));
            contract.setExpirationDate(cursor.getString(2));
            contract.setPlan(cursor.getString(3));
            contracts.add(contract);
        }

        return contracts;
    }

    private void insertBills(List<Bill> bills){
        for(Bill bill : bills){
            ContentValues values = new ContentValues();

            values.put(id, bill.getId());
            values.put(amount, bill.getAmount());
            values.put(barcode, bill.getBarcode());
            values.put(bill_name, bill.getBillName());
            values.put(expiration_date, bill.getExpirationDate());
            values.put(path_pdf, bill.getPathPdf());

            db.insert(bills_table, null, values);
        }
    }

    private void insertConnections(List<Connection> connections){
        for(Connection connection : connections){
            ContentValues values = new ContentValues();

            values.put(id, connection.getId());
            values.put(address, connection.getAddress());
            values.put(contract_id, connection.getContractId());
            values.put(blocked, connection.getBlocked());
            values.put(blocking_reason, connection.getBlockingReason());
            values.put(customer_since, connection.getCustomerSince());
            values.put(latitude, connection.getLatitude());
            values.put(longitude, connection.getLongitude());
            values.put(mac_address, connection.getMacAddress());
            values.put(username, connection.getUsername());

            db.insert(connections_table, null, values);
        }
    }

    private void insertContracts(List<Contract> contracts){
        for(Contract contract : contracts){
            ContentValues values = new ContentValues();

            values.put(id, contract.getId());
            values.put(adherence_since, contract.getAdherenceSince());
            values.put(expiration_date, contract.getExpirationDate());
            values.put(plan_, contract.getPlan());
            db.insert(contracts_table, null, values);
        }
    }

    public void insertCustomer(Customer customer){
        delete();
        ContentValues values;
        values = new ContentValues();

        values.put(id, customer.getId());
        values.put(address, customer.getAddress());
        values.put(cpf, customer.getCpf());
        values.put(email, customer.getEmail());
        values.put(fone, customer.getFone());
        values.put(name, customer.getName());
        values.put(neighborhood, customer.getNeighborhood());
        values.put(status, customer.getStatus());
        values.put(zipCode, customer.getZipCode());

        db.insert(customers_table, null, values);
    }

    public void updateConnectionBlocked(Boolean blocked){
        ContentValues values = new ContentValues();
        values.put(this.blocked, blocked);
        db.update(connections_table, values, null, null);
    }

    public void delete(){
        db.delete(bills_table, null,null);
        db.delete(connections_table, null,null);
        db.delete(contracts_table, null,null);
        db.delete(customers_table, null,null);
    }
}
