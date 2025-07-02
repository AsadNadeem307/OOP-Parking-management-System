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
