
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ParkingManagementSystem extends JFrame {
    private Vehicle[] parkedVehicles;
    private ParkingSpot[] parkingSpots;
    private JPanel mainPanel, spotPanel;
    private JTextField vehicleNumberField;
    private JComboBox<String> vehicleTypeCombo;
    
    // Fixed amounts for each vehicle type
    private static final double CAR_AMOUNT = 5.00;
    private static final double MOTORCYCLE_AMOUNT = 3.00;
    private static final double TRUCK_AMOUNT = 8.00;
    
    // Color scheme moved from static fields to instance variables
    private Color bgColor;
    private Color panelColor;
    private Color accentColor;
    private Color freeSpotColor;
    private Color occupiedSpotColor;
    private Color textColor;
    private Color buttonTextColor;
    
    public ParkingManagementSystem() {
        // Initialize colors optimized for projector visibility
        bgColor = new Color(220, 220, 220);       // Light gray background
        panelColor = new Color(180, 180, 180);    // Slightly darker gray for panels
        accentColor = new Color(0, 120, 215);     // Strong blue for buttons
        freeSpotColor = new Color(100, 200, 100); // Bright green for free spots
        occupiedSpotColor = new Color(240, 100, 100); // Bright red for occupied spots
        textColor = new Color(0, 0, 0);           // Black text for maximum contrast
        buttonTextColor = new Color(255, 255, 255); // White text for buttons
        
        // Simple array initialization
        parkedVehicles = new Vehicle[20]; // Maximum 20 vehicles
        parkingSpots = new ParkingSpot[20]; // 20 parking spots
        initializeSpots();
        setupGUI();
    }
    
    private void initializeSpots() {
        for(int i = 0; i < parkingSpots.length; i++) {
            parkingSpots[i] = new ParkingSpot(i + 1);
        }
    }
    
    private void setupGUI() {
        setTitle("Simple Parking Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = createHeaderPanel();
        JPanel parkingView = createParkingView();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(parkingView, BorderLayout.CENTER);
        
        add(mainPanel);
        setLocationRelativeTo(null);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(panelColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Parking Management");
        titleLabel.setForeground(textColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Larger font for better visibility
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createParkingView() {
        JPanel parkingView = new JPanel(new BorderLayout(10, 10));
        parkingView.setBackground(bgColor);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(bgColor);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(textColor, 2), // Thicker border
            "Vehicle Information",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16), // Larger font
            textColor
        ));
        
        setupInputComponents(inputPanel);
        
        spotPanel = new JPanel(new GridLayout(4, 5, 10, 10));
        spotPanel.setBackground(bgColor);
        spotPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(textColor, 2), // Thicker border
            "Parking Spots",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16), // Larger font
            textColor
        ));
        
        updateSpotPanel();
        
        parkingView.add(inputPanel, BorderLayout.NORTH);
        parkingView.add(spotPanel, BorderLayout.CENTER);
        
        return parkingView;
    }
    
    private void setupInputComponents(JPanel inputPanel) {
        // Change from GridLayout to a more flexible layout
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Vehicle Number components
        JLabel vehicleNumberLabel = createStyledLabel("Vehicle Number:");
        vehicleNumberField = new JTextField();
        styleTextField(vehicleNumberField);
        
        // Vehicle Type components with pricing information
        JLabel vehicleTypeLabel = createStyledLabel("Vehicle Type:");
        vehicleTypeCombo = new JComboBox<>(new String[]{"Car ($5.00)", "Motorcycle ($3.00)", "Truck ($8.00)"});
        styleComboBox(vehicleTypeCombo);
        
        // Buttons
        JButton parkButton = createStyledButton("Park Vehicle");
        JButton removeButton = createStyledButton("Remove Vehicle");
        
        parkButton.addActionListener(e -> parkVehicle());
        removeButton.addActionListener(e -> removeVehicle());
        
        // Add components with GridBagConstraints for better positioning
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        inputPanel.add(vehicleNumberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        inputPanel.add(vehicleNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        inputPanel.add(vehicleTypeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        inputPanel.add(vehicleTypeCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        inputPanel.add(parkButton, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        inputPanel.add(removeButton, gbc);
    }
    
    private void updateSpotPanel() {
        spotPanel.removeAll();
        
        for(ParkingSpot spot : parkingSpots) {
            JPanel spotBox = new JPanel(new BorderLayout());
            spotBox.setBackground(spot.isOccupied() ? occupiedSpotColor : freeSpotColor);
            spotBox.setBorder(BorderFactory.createLineBorder(textColor, 2)); // Thicker border
            
            JLabel spotLabel = new JLabel("Spot " + spot.getSpotNumber(), SwingConstants.CENTER);
            spotLabel.setForeground(textColor);
            spotLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
            spotBox.add(spotLabel, BorderLayout.NORTH);
            
            if(spot.isOccupied()) {
                // Create a panel for vehicle info
                JPanel vehicleInfoPanel = new JPanel(new GridLayout(2, 1));
                vehicleInfoPanel.setBackground(spot.isOccupied() ? occupiedSpotColor : freeSpotColor);
                
                JLabel vehicleLabel = new JLabel(spot.getParkedVehicle().getNumber(), SwingConstants.CENTER);
                vehicleLabel.setForeground(textColor);
                vehicleLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Bold font
                
                JLabel typeLabel = new JLabel(spot.getParkedVehicle().getType() + 
                                           " ($" + String.format("%.2f", spot.getParkedVehicle().getAmount()) + ")", 
                                           SwingConstants.CENTER);
                typeLabel.setForeground(textColor);
                typeLabel.setFont(new Font("Arial", Font.BOLD, 11)); // Bold font
                
                vehicleInfoPanel.add(vehicleLabel);
                vehicleInfoPanel.add(typeLabel);
                
                spotBox.add(vehicleInfoPanel, BorderLayout.CENTER);
            }
            
            spotPanel.add(spotBox);
        }
        
        spotPanel.revalidate();
        spotPanel.repaint();
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        label.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
        return label;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(buttonTextColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Larger font
        return button;
    }
    
    private void styleTextField(JTextField textField) {
        textField.setBackground(Color.WHITE); // White background for better contrast
        textField.setForeground(textColor);
        textField.setCaretColor(textColor);
        textField.setBorder(BorderFactory.createLineBorder(textColor, 1));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE); // White background for better contrast
        comboBox.setForeground(textColor);
        comboBox.setBorder(BorderFactory.createLineBorder(textColor, 1));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void parkVehicle() {
        String vehicleNumber = vehicleNumberField.getText();
        String vehicleTypeWithPrice = (String) vehicleTypeCombo.getSelectedItem();
        
        // Basic validation
        if (!validateVehicleNumber(vehicleNumber)) {
            showMessage("Vehicle number must be at least 2 alphanumeric characters", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract just the vehicle type from the combo box selection
        String vehicleType = vehicleTypeWithPrice.split(" \(\$")[0];
        
        // Get the fixed amount based on vehicle type
        double amount = getAmountForVehicleType(vehicleType);
        
        // Check if vehicle already exists
        if (checkVehicleExists(vehicleNumber)) {
            showMessage("Vehicle with number " + vehicleNumber + " is already parked", "Parking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Find available spot
        ParkingSpot spot = findAvailableSpot();
        
        if (spot == null) {
            showMessage("No parking spots available", "Parking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create vehicle and occupy spot
        Vehicle vehicle = new Vehicle(vehicleNumber, vehicleType, amount);
        if (!spot.occupy(vehicle)) {
            showMessage("Failed to occupy parking spot", "System Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Store in array
        boolean stored = false;
        for (int i = 0; i < parkedVehicles.length; i++) {
            if (parkedVehicles[i] == null) {
                parkedVehicles[i] = vehicle;
                stored = true;
                break;
            }
        }
        
        if (!stored) {
            showMessage("Failed to store vehicle in system", "System Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        updateSpotPanel();
        
        showMessage("Vehicle parked successfully at spot " + spot.getSpotNumber() + 
                   "\nVehicle Type: " + vehicleType + 
                   "\nParking Fee: $" + String.format("%.2f", amount), 
                   "Success", JOptionPane.INFORMATION_MESSAGE);
        
        vehicleNumberField.setText("");
    }
    
    private void removeVehicle() {
        String vehicleNumber = vehicleNumberField.getText();
        
        // Basic validation
        if (!validateVehicleNumber(vehicleNumber)) {
            showMessage("Vehicle number must be at least 2 alphanumeric characters", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Find vehicle in the system
        int vehicleIndex = findVehicleIndex(vehicleNumber);
        if (vehicleIndex == -1) {
            showMessage("Vehicle with number " + vehicleNumber + " is not parked here", "Parking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Remove from array
        Vehicle vehicle = parkedVehicles[vehicleIndex];
        parkedVehicles[vehicleIndex] = null;
        
        // Find and vacate the spot
        boolean spotVacated = false;
        int spotNumber = 0;
        
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getParkedVehicle() != null && 
                spot.getParkedVehicle().getNumber().equals(vehicleNumber)) {
                
                spotNumber = spot.getSpotNumber();
                if (!spot.vacate()) {
                    showMessage("Failed to vacate parking spot", "System Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                spotVacated = true;
                break;
            }
        }
        
        if (!spotVacated) {
            // This should never happen if our data is consistent
            showMessage("Vehicle found in system but not in any parking spot", "System Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        updateSpotPanel();
        
        showMessage("Vehicle removed successfully from spot " + spotNumber, 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        vehicleNumberField.setText("");
    }
    
    private int findVehicleIndex(String vehicleNumber) {
        for (int i = 0; i < parkedVehicles.length; i++) {
            if (parkedVehicles[i] != null && parkedVehicles[i].getNumber().equals(vehicleNumber)) {
                return i;
            }
        }
        return -1;
    }
    
    private ParkingSpot findAvailableSpot() {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied()) {
                return spot;
            }
        }
        return null;
    }
    
    private boolean validateVehicleNumber(String vehicleNumber) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - alphanumeric with minimum 2 characters
        return vehicleNumber.matches("^[A-Za-z0-9]{2,}$");
    }
    
    private boolean checkVehicleExists(String vehicleNumber) {
        return findVehicleIndex(vehicleNumber) != -1;
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    private double getAmountForVehicleType(String vehicleType) {
        switch(vehicleType) {
            case "Car":
                return CAR_AMOUNT;
            case "Motorcycle":
                return MOTORCYCLE_AMOUNT;
            case "Truck":
                return TRUCK_AMOUNT;
            default:
                return 0.0; // Default case, should never happen
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel without exception handling
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {
                // Silently continue with default look and feel
            }
            new ParkingManagementSystem().setVisible(true);
        });
    }
}

class Vehicle {
    private String number;
    private String type;
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
        this.occupied = false;
    }
    
    public boolean occupy(Vehicle vehicle) {
        if (this.occupied || vehicle == null) {
            return false;
        }
        this.parkedVehicle = vehicle;
        this.occupied = true;
        return true;
    }
    
    public boolean vacate() {
        if (!this.occupied) {
            return false;
        }
        this.parkedVehicle = null;
        this.occupied = false;
        return true;
    }
    
    public boolean isOccupied() { return occupied; }
    public int getSpotNumber() { return spotNumber; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}