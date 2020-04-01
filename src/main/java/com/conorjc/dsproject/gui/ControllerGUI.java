package com.conorjc.dsproject.gui;

import com.proto.vpn.VpnRequest;
import com.proto.vpn.VpnResponse;
import com.proto.vpn.VpnServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerGUI implements ActionListener {

    private JTextField reply;

    private JPanel getVpnService() {

        JPanel panel = new JPanel();

        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.X_AXIS);

        JButton button = new JButton("Check Vpn Status...");
        button.addActionListener(this);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        reply = new JTextField("", 10);
        reply.setEditable(false);
        panel.add(reply);

        panel.setLayout(boxlayout);

        return panel;

    }

    public static void main(String[] args) {

        ControllerGUI gui = new ControllerGUI();

        gui.build();
    }

    public void build() {

        JFrame frame = new JFrame("Smart Home Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the panel to add buttons
        JPanel panel = new JPanel();

        // Set the BoxLayout to be X_AXIS: from left to right
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

        panel.setLayout(boxlayout);

        // Set border for the panel
        panel.setBorder(new EmptyBorder(new Insets(50, 100, 50, 100)));

        panel.add(getVpnService());


        // Set size for the frame
        frame.setSize(300, 300);

        // Set the window to be visible as the default to be false
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String label = button.getActionCommand();


        if (label.equals("Check Vpn Status...")) {
            System.out.println("Please Wait ...");


            /*
             *
             */
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();
            VpnServiceGrpc.VpnServiceBlockingStub blockingStub = VpnServiceGrpc.newBlockingStub(channel);

            //preparing message to send
            VpnRequest request = VpnRequest.newBuilder().build();

            //retrieving reply from service
            VpnResponse response = blockingStub.vpnStatus(request);

            reply.getText();

        } else {

        }
    }
}