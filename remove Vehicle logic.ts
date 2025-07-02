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
