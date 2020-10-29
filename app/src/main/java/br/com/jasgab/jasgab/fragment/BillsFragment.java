package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.list.BillAdapter;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class BillsFragment extends Fragment {
    View view;
    List<Bill> bills;
    BillAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bills, container, false);
        JasgabUtils.setActionBarHome("Faturas", view, requireContext(), requireActivity());

        RecyclerView mRecyclerView = view.findViewById(R.id.bills_recicleview_bills);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(layoutManager);

        bills = new CustomerDAO(requireContext()).selectBills();
        bills = JasgabUtils.orderBills(bills);
        mAdapter = new BillAdapter(bills, requireContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.update();
    }
}
