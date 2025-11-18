
public enum Rating {
	G,
	PG,
	PG_13,
	NC_17,
	R;
	
	@Override
	public String toString() {
		
		return switch (this) {
			case PG_13 -> "PG-13";
			case NC_17 -> "NC-17";
			default -> name();
		};
	}
}

