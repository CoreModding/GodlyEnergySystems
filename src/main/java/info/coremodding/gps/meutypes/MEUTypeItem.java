package info.coremodding.gps.meutypes;

import info.coremodding.gps.api.meu.MEUPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.minecraft.item.ItemStack;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * @author James An MEU packet carrier for items
 */
public class MEUTypeItem extends MEUPacket {

	/**
	 * @return The contents of the packet
	 */
	public ItemStack[] getContents() {
		ByteArrayInputStream in = new ByteArrayInputStream(this.getMeta()
				.getBytes());
		ObjectInputStream is;
		try {
			is = new ObjectInputStream(in);
			return (ItemStack[]) is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		return "items";
	}

	/**
	 * @param contents
	 *            The contents to set
	 * @return This
	 */
	public MEUTypeItem setContents(ItemStack... contents) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (ObjectOutputStream os = new ObjectOutputStream(out)) {
			os.writeObject(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setMeta(String.valueOf(out.toByteArray()));
		return this;
	}
}
