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
