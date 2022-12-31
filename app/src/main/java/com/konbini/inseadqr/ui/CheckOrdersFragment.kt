package com.konbini.inseadqr.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.konbini.inseadqr.R
import com.konbini.inseadqr.adapter.`OrderListAdapter`
import com.konbini.inseadqr.data.CustomerOrdersByStatusModelItem
import com.konbini.inseadqr.databinding.FragmentCheckOrdersBinding
import com.konbini.inseadqr.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CheckOrdersFragment : Fragment() {
    private var _binding: FragmentCheckOrdersBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var orderListAdapter: OrderListAdapter
    private val items: ArrayList<CustomerOrdersByStatusModelItem> = arrayListOf()
    private lateinit var customerId: String
    private var isForOrderStatus: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            customerId = it.getString("customer_id") ?: ""
            customerId.let { it1 -> mainViewModel.checkCustomerOrderByStatus("ready", it1) }
            isForOrderStatus = it.getBoolean("is_for_order_status", false)
        }
        binding.ordersList.layoutManager = LinearLayoutManager(requireContext())
        orderListAdapter = OrderListAdapter(isForOrderStatus, items, object : OrderListAdapter.OnOrderSelectionChanged {
                    override fun onItemSelectionChanged(total: Double) {
                        if (!isForOrderStatus) {
                            if (total == 0.0) {
                                binding.payButton.visibility = View.GONE
                            } else {
                                binding.payButton.visibility = View.VISIBLE
                                binding.payButton.text = String.format("Pay $ %.2f", total)
                            }
                        }
                    }
                })
        binding.ordersList.adapter = orderListAdapter


        (binding.ordersList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.ordersList.setHasFixedSize(true)

        binding.payButton.setOnClickListener {
            binding.payButton.visibility = View.GONE
            if (orderListAdapter.selectedOrders.size > 0) {
                mainViewModel.updateOrderStatus(orderListAdapter.selectedOrders)
            }
        }

        mainViewModel.isPendingOrdersExist.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it) {
                    items.clear()
                    Log.i("CheckOrdersFragment", "${mainViewModel.orderItems}")
                    items.addAll(mainViewModel.orderItems)
                    binding.ordersList.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.desc.visibility = View.GONE
                    binding.tableHeading.visibility = View.VISIBLE
                    binding.backButton.visibility = View.VISIBLE

                    if (!isForOrderStatus) {
                        binding.paymentHint.visibility = View.VISIBLE
                    }
                    binding.tableHeading.text =
                        String.format("Un-Paid orders (Table $customerId)")
                    orderListAdapter.notifyDataSetChanged()
                } else {
                    if (isForOrderStatus) {
                        lifecycleScope.launch {
                            binding.progressBar.visibility = View.GONE
                            delay(100)
                            binding.desc.text = getString(R.string.no_unpaid_orders)
                            binding.backButton.visibility = View.VISIBLE
                        }
                    } else {
                        // Show no unpaid orders
                        lifecycleScope.launch {
                            delay(100)
                            binding.desc.text = getString(R.string.no_unpaid_orders)
                            delay(100)
                            binding.desc.text = getString(R.string.check_out_in_progress)
                            mainViewModel.disableTemporaryLoginForUser(customerId)
                        }
                    }
                }
            }
        }

        mainViewModel.isOrderStatusUpdated.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it) {
                    binding.ordersList.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.desc.visibility = View.VISIBLE
                    binding.tableHeading.visibility = View.GONE
                    binding.paymentHint.visibility = View.INVISIBLE

                    lifecycleScope.launch {
                        binding.desc.text = getString(R.string.payment_successful)
                        delay(1000)
                        binding.desc.text = getString(R.string.fetching_outstanding_orders)
                        mainViewModel.checkCustomerOrderByStatus("ready", customerId)
                    }
                }
            }
        }

        mainViewModel.isLogoutCompleted.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it) {
                    findNavController().navigateUp()
                }
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}