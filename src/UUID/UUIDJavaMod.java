package UUID;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.math.Rand;
import arc.scene.ui.TextField;
import arc.util.Time;
import arc.util.serialization.Base64Coder;
import mindustry.game.EventType.*;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.fragments.MenuFragment;

import static mindustry.Vars.ui;

public class UUIDJavaMod extends Mod {

    public UUIDJavaMod() {
        Events.on(ClientLoadEvent.class, e -> {
            Time.runTask(10f, this::addMenuButton);
        });
    }

    private void showPlayerInfoDefaultDialog() {
        TextField exampleUUID = new TextField();
        String modUuid = Core.settings.getString("uuid", "");
        exampleUUID.setMessageText("@uuid.example");
        BaseDialog dialog = new BaseDialog("@uuid.title");
        dialog.cont.table(table -> {
            table.add("Player UUID").color(Pal.accent).style(Styles.techLabel).fontScale(1.5f).pad(40f).row();
            table.add(Core.bundle.format("uuid.your-uuid", modUuid)).color(Color.lightGray).left().row();
            table.add("@uuid.description").color(Color.lightGray).left().row();
            table.table(buttons -> {
                buttons.button("Github",() -> {
                    Core.app.openURI("https://github.com/Kashkinzey/ChangeUUID");
                }).color(Color.white).size(200f, 50f).pad(5f).row();
                buttons.button("@uuid.copy-uuid", () -> {
                    Core.app.setClipboardText(modUuid);
                    ui.showInfo("@uuid.copy-success");
                }).size(200f, 50f).pad(5f).row();
                buttons.button("@uuid.random-uuid", () -> {
                    byte[] result = new byte[8];
                    new Rand().nextBytes(result);
                    String uuid = new String(Base64Coder.encode(result));
                    Core.settings.put("uuid", uuid);
                    dialog.hide();
                    showPlayerInfoDefaultDialog();
                }).size(200f, 50f).pad(5f).row();
                buttons.button("@uuid.save-uuid", () -> {
                    Core.settings.put("uuid", exampleUUID.getText().trim());
                    dialog.hide();
                    showPlayerInfoDefaultDialog();
                }).size(200f, 50f).pad(5f).row();
            }).pad(20f).row();
            table.add("@uuid.example-uuid").left().row();
            table.add(exampleUUID).width(250f).pad(10f).row();
            table.add("@uuid.warning").color(Color.red).left().row();
            table.add("@uuid.note").color(Color.green).left().row();
        });
        dialog.addCloseButton();
        dialog.show();
    }

    private void addMenuButton() {
        MenuFragment menu = ui.menufrag;
        menu.addButton("@uuid.menu-button", Icon.grid, this::showPlayerInfoDefaultDialog);
    }
}