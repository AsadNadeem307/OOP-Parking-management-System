import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ParkingManagementSystem extends JFrame {
    private Vehicle[] parkedVehicles = new Vehicle[20];
    private ParkingSpot[] parkingSpots = new ParkingSpot[20];
    private JPanel mainPanel, spotPanel;
    private JTextField vehicleNumberField;
    private JComboBox<String> vehicleTypeCombo;

    private static final double CAR_AMOUNT = 5.00;
    private static final double MOTORCYCLE_AMOUNT = 3.00;
    private static final double TRUCK_AMOUNT = 8.00;

    private Color bgColor = new Color(220, 220, 220);
    private Color panelColor = new Color(180, 180, 180);
    private Color accentColor = new Color(0, 120, 215);
    private Color freeSpotColor = new Color(100, 200, 100);
    private Color occupiedSpotColor = new Color(240, 100, 100);
    private Color textColor = new Color(0, 0, 0);
    private Color buttonTextColor = new Color(255, 255, 255);

    public ParkingManagementSystem() {
        for (int i = 0; i < 20; i++) {
            parkingSpots[i] = new ParkingSpot(i + 1);
        }
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Simple Parking Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createParkingView(), BorderLayout.CENTER);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(panelColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Parking Management", SwingConstants.CENTER);
        titleLabel.setForeground(textColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createParkingView() {
        JPanel parkingView = new JPanel(new BorderLayout(10, 10));
        parkingView.setBackground(bgColor);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(bgColor);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(textColor, 2),
                "Vehicle Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                textColor
        ));
        setupInputComponents(inputPanel);

        spotPanel = new JPanel(new GridLayout(4, 5, 10, 10));
        spotPanel.setBackground(bgColor);
        spotPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(textColor, 2),
                "Parking Spots",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                textColor
        ));
        updateSpotPanel();

        parkingView.add(inputPanel, BorderLayout.NORTH);
        parkingView.add(spotPanel, BorderLayout.CENTER);

        return parkingView;
    }

    private void setupInputComponents(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel vehicleNumberLabel = createStyledLabel("Vehicle Number:");
        vehicleNumberField = new JTextField();
        styleTextField(vehicleNumberField);

        JLabel vehicleTypeLabel = createStyledLabel("Vehicle Type:");
        vehicleTypeCombo = new JComboBox<>(new String[]{"Car ($5.00)", "Motorcycle ($3.00)", "Truck ($8.00)"});
        styleComboBox(vehicleTypeCombo);

        JButton parkButton = createStyledButton("Park Vehicle");
        JButton removeButton = createStyledButton("Remove Vehicle");

        parkButton.addActionListener(e -> parkVehicle());
        removeButton.addActionListener(e -> removeVehicle());

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(vehicleNumberLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(vehicleNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(vehicleTypeLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(vehicleTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(parkButton, gbc);

        gbc.gridx = 2;
        panel.add(removeButton, gbc);
    }

    private void updateSpotPanel() {
        spotPanel.removeAll();

        for (ParkingSpot spot : parkingSpots) {
            JPanel spotBox = new JPanel(new BorderLayout());
            spotBox.setBackground(spot.isOccupied() ? occupiedSpotColor : freeSpotColor);
            spotBox.setBorder(BorderFactory.createLineBorder(textColor, 2));

            JLabel spotLabel = new JLabel("Spot " + spot.getSpotNumber(), SwingConstants.CENTER);
            spotLabel.setForeground(textColor);
            spotLabel.setFont(new Font("Arial", Font.BOLD, 14));
            spotBox.add(spotLabel, BorderLayout.NORTH);

            if (spot.isOccupied()) {
                JPanel vehicleInfoPanel = new JPanel(new GridLayout(2, 1));
                vehicleInfoPanel.setBackground(occupiedSpotColor);

                JLabel numberLabel = new JLabel(spot.getParkedVehicle().getNumber(), SwingConstants.CENTER);
                JLabel typeLabel = new JLabel(spot.getParkedVehicle().getType() + 
                    " ($" + String.format("%.2f", spot.getParkedVehicle().getAmount()) + ")", SwingConstants.CENTER);

                numberLabel.setForeground(textColor);
                typeLabel.setForeground(textColor);

                numberLabel.setFont(new Font("Arial", Font.BOLD, 12));
                typeLabel.setFont(new Font("Arial", Font.BOLD, 11));

                vehicleInfoPanel.add(numberLabel);
                vehicleInfoPanel.add(typeLabel);

                spotBox.add(vehicleInfoPanel, BorderLayout.CENTER);
            }

            spotPanel.add(spotBox);
        }

        spotPanel.revalidate();
        spotPanel.repaint();
    }

    private void parkVehicle() {
        String number = vehicleNumberField.getText().trim();
        if (!number.matches("^[A-Za-z0-9]{2,}$")) return;

        String selected = (String) vehicleTypeCombo.getSelectedItem();
        String type = selected.split(" ")[0];
        double amount = getAmount(type);

        if (findVehicleIndex(number) != -1) return;

        ParkingSpot spot = findAvailableSpot();
        if (spot == null) return;

        Vehicle vehicle = new Vehicle(number, type, amount);
        spot.occupy(vehicle);

        for (int i = 0; i < 20; i++) {
            if (parkedVehicles[i] == null) {
                parkedVehicles[i] = vehicle;
                break;
            }
        }

        updateSpotPanel();
        vehicleNumberField.setText("");
    }

    private void removeVehicle() {
        String number = vehicleNumberField.getText().trim();
        if (!number.matches("^[A-Za-z0-9]{2,}$")) return;

        int index = findVehicleIndex(number);
        if (index == -1) return;

        parkedVehicles[index] = null;

        for (ParkingSpot spot : parkingSpots) {
            if (spot.getParkedVehicle() != null &&
                spot.getParkedVehicle().getNumber().equals(number)) {
                spot.vacate();
                break;
            }
        }

        updateSpotPanel();
        vehicleNumberField.setText("");
    }

    private ParkingSpot findAvailableSpot() {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied()) return spot;
        }
        return null;
    }

    private int findVehicleIndex(String number) {
        for (int i = 0; i < 20; i++) {
            if (parkedVehicles[i] != null &&
                parkedVehicles[i].getNumber().equals(number)) return i;
        }
        return -1;
    }

    private double getAmount(String type) {
        if (type.equals("Car")) return CAR_AMOUNT;
        if (type.equals("Motorcycle")) return MOTORCYCLE_AMOUNT;
        if (type.equals("Truck")) return TRUCK_AMOUNT;
        return 0.0;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(buttonTextColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setForeground(textColor);
        field.setCaretColor(textColor);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(textColor, 1));
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setForeground(textColor);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
        box.setBorder(BorderFactory.createLineBorder(textColor, 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParkingManagementSystem().setVisible(true));
    }
}

class Vehicle {
    private String number, type;
    private double amount;

    public Vehicle(String number, String type, double amount) {
        this.number = number;
        this.type = type;
        this.amount = amount;
    }

    public String getNumber() { return number; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
}

class ParkingSpot {
    private int spotNumber;
    private Vehicle parkedVehicle;
    private boolean occupied;

    public ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
    }

    public boolean occupy(Vehicle vehicle) {
        if (occupied) return false;
        this.parkedVehicle = vehicle;
        occupied = true;
        return true;
    }

    public boolean vacate() {
        if (!occupied) return false;
        parkedVehicle = null;
        occupied = false;
        return true;
    }

    public boolean isOccupied() { return occupied; }
    public int getSpotNumber() { return spotNumber; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}
