package com.konbini.inseadqr.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.konbini.inseadqr.R
import com.konbini.inseadqr.adapter.TableListAdapter
import com.konbini.inseadqr.data.TableData
import com.konbini.inseadqr.databinding.FragmentHomeBinding
import com.konbini.inseadqr.datapoller.DataPoller
import com.konbini.inseadqr.repositories.MainRepository
import com.konbini.inseadqr.utils.PrefUtil
import com.konbini.inseadqr.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var selectedTable: String = ""
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var tableListAdapter: TableListAdapter
    lateinit var listOfTables: ArrayList<TableData>
    private var DELAY_TIME = 1000L

    @Inject
    lateinit var mainRepository: MainRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOfTables = arrayListOf(
            TableData("3624", "Table 1"),
            TableData("3625", "Table 2"),
            TableData("3626", "Table 3"),
            TableData("3627", "Table 4"),
            TableData("3628", "Table 5"),
            TableData("3629", "Table 6"),
            TableData("3630", "Table 7"),
            TableData("3631", "Table 8"),
            TableData("3632", "Table 9"),
            TableData("3634", "Table 10"),
            TableData("3635", "Table 11"),
            TableData("3636", "Table 12"),
            TableData("3637", "Table 13"),
            TableData("3638", "Table 14"),
            TableData("3639", "Table 15")
        )
        tableListAdapter = TableListAdapter(listOfTables, object :
            TableListAdapter.OnTableSelectedListener {
            override fun onTableItemSelected(id: String, selected: Boolean) {
                selectedTable = if (selected) {
                    id
                } else {
                    ""
                }
            }
        }, requireContext())

        binding.tableList.adapter = tableListAdapter
        mainViewModel.isPendingOrdersExist.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it) {
                    // Show Message that previous orders exist
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.unpaid_orders_exist_for_this_table),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val bundle = Bundle()
                    bundle.putString("customer_id", selectedTable)
                    view.findNavController().navigate(R.id.action_home_to_check_in, bundle)
                }
            }
        }
        binding.btnCheckIn.setOnClickListener {
            if (validInput()) {
                // Check for pending orders in
                // If there are pending orders then show error that table has pending orders
                // If there are no pending orders then show the check in
                mainViewModel.checkCustomerOrderByStatus(
                    "ready",
                    selectedTable
                )
            }
        }

        binding.btnCheckOut.setOnClickListener {
            if (validInput()) {
                val bundle = Bundle()
                bundle.putString("customer_id", selectedTable)
                bundle.putBoolean("is_for_order_status", false)
                view.findNavController().navigate(R.id.action_home_to_check_orders, bundle)
                selectedTable = ""
            }
        }

        binding.btnCheckOrderStatus.setOnClickListener {
            if (validInput()) {
                val bundle = Bundle()
                bundle.putString("customer_id", selectedTable)
                bundle.putBoolean("is_for_order_status", true)
                view.findNavController().navigate(R.id.action_home_to_check_orders, bundle)
                selectedTable = ""
            }
        }

        binding.btnSettings1.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_settings)
            selectedTable = ""
        }
        getTableStatuses()
    }

    @ExperimentalCoroutinesApi
    private fun getTableStatuses() {
        val dataPoller = DataPoller(mainViewModel, mainRepository, Dispatchers.IO)
        when (mainViewModel.pollingState.value) {
            "INACTIVE" -> {
                mainViewModel.pollingState.value = "ACTIVE"
                val url =
                    PrefUtil.getString("AppSettings.Cloud.Host") + "/wp-json/wp/v2/ktl/check-login-status?role=table"
                val data = dataPoller.poll(DELAY_TIME, url)
                CoroutineScope(Dispatchers.IO).launch {
                    data.collect { res ->
                        Log.i("HomeFragment", "${res.data?.data}")
                        listOfTables.forEach {
                            res.data?.data?.map { tableData ->
                                if (it.id == tableData.userId) {
                                    it.status = tableData.status
                                }
                            }
                        }
                        withContext(Dispatchers.Main) {
                            tableListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            "ACTIVE" -> {
                mainViewModel.pollingState.value = "INACTIVE"
            }
        }
    }

    private fun validInput(): Boolean {
        return selectedTable.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainViewModel.isPendingOrdersExist.removeObservers(this)
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.pollingState.value = "ACTIVE"
    }
}