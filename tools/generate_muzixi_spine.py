"""Generate the Spine 3.4 rig consumed by Slay the Spire's bundled runtime."""

from __future__ import annotations

import json
import math
from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
CHARACTER_DIR = (
    ROOT
    / "src"
    / "main"
    / "resources"
    / "NineSwordResources"
    / "img"
    / "muzixi"
    / "character"
)
SOURCE = CHARACTER_DIR / "muzixi.png"
OUTPUT_DIR = CHARACTER_DIR / "spine"
TEXTURE_NAME = "muzixi.png"
ATLAS_NAME = "muzixi.atlas"
SKELETON_NAME = "muzixi.json"


TEXTURE_SIZE = (400, 600)
REFERENCE_ALPHA_BBOX = (15, 59, 381, 600)

# The rig was authored against REFERENCE_ALPHA_BBOX. These values are updated
# from the current source image so resizing the figure inside the unchanged
# 400x600 canvas does not require manually rewriting every bone and weight.
art_scale = 1.0
art_offset_x = 0.0
art_offset_y = 0.0


def load_texture(path: Path) -> Image.Image:
    """Normalize the source art to the dimensions used by the Spine mesh."""
    image = Image.open(path).convert("RGBA")
    if image.size != TEXTURE_SIZE:
        image = image.resize(TEXTURE_SIZE, Image.Resampling.LANCZOS)
    return image


def configure_art_transform(texture: Image.Image) -> None:
    """Map the reference pose coordinates onto the current alpha bounds."""
    global art_scale, art_offset_x, art_offset_y

    current_bbox = texture.getchannel("A").getbbox()
    if current_bbox is None:
        raise ValueError(f"Source texture has no visible pixels: {SOURCE}")

    reference_left, reference_top, reference_right, reference_bottom = REFERENCE_ALPHA_BBOX
    current_left, current_top, current_right, current_bottom = current_bbox
    width_scale = (current_right - current_left) / (reference_right - reference_left)
    height_scale = (current_bottom - current_top) / (reference_bottom - reference_top)
    art_scale = min(width_scale, height_scale)

    reference_center_x = (reference_left + reference_right) / 2.0
    current_center_x = (current_left + current_right) / 2.0
    art_offset_x = current_center_x - reference_center_x * art_scale
    art_offset_y = current_bottom - reference_bottom * art_scale


def to_current_point(point: tuple[float, float]) -> tuple[float, float]:
    x, y = point
    return x * art_scale + art_offset_x, y * art_scale + art_offset_y


def to_reference_point(point: tuple[float, float]) -> tuple[float, float]:
    x, y = point
    return (x - art_offset_x) / art_scale, (y - art_offset_y) / art_scale


def reference_world_to_current(point: tuple[float, float]) -> tuple[float, float]:
    """Transform a bottom-centred Spine world point through image space."""
    world_x, world_y = point
    pixel_x, pixel_y = to_current_point((world_x + 200.0, 600.0 - world_y))
    return pixel_x - 200.0, 600.0 - pixel_y


def point_segment_distance(
    point: tuple[float, float], start: tuple[float, float], end: tuple[float, float]
) -> float:
    px, py = point
    sx, sy = start
    ex, ey = end
    dx = ex - sx
    dy = ey - sy
    denominator = dx * dx + dy * dy
    if denominator == 0:
        return math.hypot(px - sx, py - sy)
    amount = max(0.0, min(1.0, ((px - sx) * dx + (py - sy) * dy) / denominator))
    nearest_x = sx + amount * dx
    nearest_y = sy + amount * dy
    return math.hypot(px - nearest_x, py - nearest_y)


def line_score(
    point: tuple[float, float], start: tuple[float, float], end: tuple[float, float], radius: float
) -> float:
    distance = point_segment_distance(point, start, end)
    return math.exp(-(distance * distance) / (2.0 * radius * radius))


def ellipse_score(
    point: tuple[float, float], center: tuple[float, float], radii: tuple[float, float]
) -> float:
    x, y = point
    cx, cy = center
    rx, ry = radii
    distance_squared = ((x - cx) / rx) ** 2 + ((y - cy) / ry) ** 2
    return math.exp(-2.0 * distance_squared)


def create_bones() -> tuple[list[dict], dict[str, tuple[float, float]]]:
    reference_world_positions = {
        "root": (0.0, 0.0),
        # Coordinates below follow the current 400x600 battle pose.  They are
        # kept in world space (origin at the bottom centre of the texture).
        "pelvis": (15.0, 245.0),
        "torso": (20.0, 320.0),
        "head": (45.0, 445.0),
        "back_arm": (55.0, 345.0),
        "back_forearm": (115.0, 370.0),
        "front_arm": (5.0, 345.0),
        "front_forearm": (-55.0, 370.0),
        "front_hand": (-105.0, 370.0),
        "green_hair_1": (-10.0, 445.0),
        "green_hair_2": (-65.0, 385.0),
        "purple_hair_1": (75.0, 440.0),
        "purple_hair_2": (110.0, 380.0),
        "green_skirt_1": (0.0, 275.0),
        "green_skirt_2": (-55.0, 180.0),
        "purple_skirt_1": (55.0, 275.0),
        "purple_skirt_2": (105.0, 160.0),
        "green_leg": (-50.0, 180.0),
        "purple_leg": (80.0, 180.0),
        "vine_1": (-125.0, 390.0),
        "vine_2": (-160.0, 405.0),
        "vine_3": (-185.0, 420.0),
    }
    parents = {
        "pelvis": "root",
        "torso": "pelvis",
        "head": "torso",
        "back_arm": "torso",
        "back_forearm": "back_arm",
        "front_arm": "torso",
        "front_forearm": "front_arm",
        "front_hand": "front_forearm",
        "green_hair_1": "head",
        "green_hair_2": "green_hair_1",
        "purple_hair_1": "head",
        "purple_hair_2": "purple_hair_1",
        "green_skirt_1": "pelvis",
        "green_skirt_2": "green_skirt_1",
        "purple_skirt_1": "pelvis",
        "purple_skirt_2": "purple_skirt_1",
        "green_leg": "pelvis",
        "purple_leg": "pelvis",
        "vine_1": "front_hand",
        "vine_2": "vine_1",
        "vine_3": "vine_2",
    }
    lengths = {
        "torso": 125,
        "head": 60,
        "back_arm": 58,
        "back_forearm": 58,
        "front_arm": 62,
        "front_forearm": 68,
        "front_hand": 30,
        "green_hair_1": 60,
        "green_hair_2": 60,
        "purple_hair_1": 60,
        "purple_hair_2": 60,
        "green_skirt_1": 95,
        "green_skirt_2": 100,
        "purple_skirt_1": 105,
        "purple_skirt_2": 115,
        "green_leg": 140,
        "purple_leg": 140,
        "vine_1": 58,
        "vine_2": 58,
        "vine_3": 58,
    }

    world_positions = {
        name: (0.0, 0.0) if name == "root" else reference_world_to_current(position)
        for name, position in reference_world_positions.items()
    }
    bones = [{"name": "root"}]
    for name, parent in parents.items():
        world_x, world_y = world_positions[name]
        parent_x, parent_y = world_positions[parent]
        bone = {
            "name": name,
            "parent": parent,
            "x": round(world_x - parent_x, 4),
            "y": round(world_y - parent_y, 4),
        }
        if name in lengths:
            bone["length"] = round(lengths[name] * art_scale, 4)
        bones.append(bone)
    return bones, world_positions


def scores_for_vertex(px: float, py: float) -> dict[str, float]:
    px, py = to_reference_point((px, py))
    point = (px, py)
    scores = {
        "pelvis": 0.24,
        "torso": 1.15 * ellipse_score(point, (220, 280), (78, 125)),
        "head": 1.4 * ellipse_score(point, (245, 155), (65, 88)),
        "back_arm": 1.65 * line_score(point, (255, 255), (315, 230), 23),
        "back_forearm": 1.8 * line_score(point, (315, 230), (380, 225), 21),
        "front_arm": 1.7 * line_score(point, (205, 255), (145, 230), 25),
        "front_forearm": 1.9 * line_score(point, (145, 230), (95, 230), 24),
        "front_hand": 1.9 * ellipse_score(point, (82, 230), (38, 32)),
        "green_hair_1": 1.45 * ellipse_score(point, (190, 155), (78, 95)),
        "green_hair_2": 1.5 * ellipse_score(point, (135, 215), (68, 78)),
        "purple_hair_1": 1.45 * ellipse_score(point, (275, 160), (62, 92)),
        "purple_hair_2": 1.45 * ellipse_score(point, (305, 220), (58, 84)),
        "green_skirt_1": 1.25 * ellipse_score(point, (195, 335), (92, 90)),
        "green_skirt_2": 1.55 * ellipse_score(point, (125, 420), (95, 112)),
        "purple_skirt_1": 1.3 * ellipse_score(point, (250, 340), (83, 98)),
        "purple_skirt_2": 1.55 * ellipse_score(point, (300, 435), (105, 125)),
        "green_leg": 1.8 * line_score(point, (150, 420), (120, 570), 33),
        "purple_leg": 1.8 * line_score(point, (285, 420), (305, 570), 36),
        "vine_1": 1.75 * line_score(point, (85, 225), (50, 210), 20),
        "vine_2": 1.8 * line_score(point, (50, 210), (20, 195), 24),
        "vine_3": 1.85 * line_score(point, (20, 195), (5, 175), 25),
    }

    # Keep the face on the head bone while allowing each hair mass to lag behind.
    if 195 <= px <= 295 and 95 <= py <= 210:
        scores["head"] += 1.2
    # The hips and waist should remain stable while adjacent cloth moves.
    if 150 <= px <= 280 and 270 <= py <= 360:
        scores["pelvis"] += 0.65
    # Prevent distant transparent mesh corners from inheriting large rotations.
    visible_center = ellipse_score(point, (225, 315), (245, 345))
    scores["root"] = 0.12 + (1.0 - visible_center) * 0.8
    return scores


def influences_for_vertex(
    px: float,
    py: float,
    bone_indexes: dict[str, int],
    world_positions: dict[str, tuple[float, float]],
) -> list[float]:
    scores = scores_for_vertex(px, py)
    strongest = sorted(scores.items(), key=lambda item: item[1], reverse=True)[:3]
    total = sum(score for _, score in strongest)
    world_x = px - 200.0
    world_y = 600.0 - py
    values: list[float] = [float(len(strongest))]
    for name, score in strongest:
        pivot_x, pivot_y = world_positions[name]
        values.extend(
            [
                float(bone_indexes[name]),
                round(world_x - pivot_x, 4),
                round(world_y - pivot_y, 4),
                round(score / total, 6),
            ]
        )
    return values


def create_mesh(
    bone_indexes: dict[str, int], world_positions: dict[str, tuple[float, float]]
) -> dict:
    reference_x_coordinates = [0, 30, 60, 90, 120, 145, 165, 185, 205, 225, 250, 275, 300, 330, 365, 400]
    reference_y_coordinates = [0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 340, 380, 420, 460, 500, 540, 570, 600]
    x_coordinates = sorted({
        0.0,
        400.0,
        *(round(to_current_point((x, 0))[0], 4) for x in reference_x_coordinates),
    })
    y_coordinates = sorted({
        0.0,
        600.0,
        *(round(to_current_point((0, y))[1], 4) for y in reference_y_coordinates),
    })

    uvs: list[float] = []
    vertices: list[float] = []
    for py in y_coordinates:
        for px in x_coordinates:
            uvs.extend([round(px / 400.0, 6), round(py / 600.0, 6)])
            vertices.extend(influences_for_vertex(px, py, bone_indexes, world_positions))

    triangles: list[int] = []
    columns = len(x_coordinates)
    for row in range(len(y_coordinates) - 1):
        for column in range(columns - 1):
            top_left = row * columns + column
            top_right = top_left + 1
            bottom_left = top_left + columns
            bottom_right = bottom_left + 1
            triangles.extend([top_left, bottom_left, top_right, top_right, bottom_left, bottom_right])

    return {
        "type": "mesh",
        "path": "muzixi",
        "uvs": uvs,
        "triangles": triangles,
        "vertices": vertices,
        "width": 400,
        "height": 600,
    }


def frame(time: float, *, angle: float | None = None, x: float | None = None, y: float | None = None) -> dict:
    value: dict[str, float | list[float]] = {"time": time}
    if angle is not None:
        value["angle"] = angle
    if x is not None:
        value["x"] = x
    if y is not None:
        value["y"] = y
    return value


def rotate(*keys: tuple[float, float]) -> dict:
    return {"rotate": [frame(time, angle=angle) for time, angle in keys]}


def translate(*keys: tuple[float, float, float]) -> dict:
    return {"translate": [frame(time, x=x, y=y) for time, x, y in keys]}


def merge(*timelines: dict) -> dict:
    result: dict = {}
    for timeline in timelines:
        result.update(timeline)
    return result


def create_animations() -> dict:
    idle_end = 3.2
    idle = {
        "pelvis": translate((0, 0, 0), (1.6, 0, 3.2), (idle_end, 0, 0)),
        "torso": merge(
            rotate((0, -1.0), (1.6, 1.1), (idle_end, -1.0)),
            translate((0, 0, 0), (1.6, 0, 1.9), (idle_end, 0, 0)),
        ),
        "head": rotate((0, 1.2), (1.6, -1.6), (idle_end, 1.2)),
        "back_arm": rotate((0, -1.6), (1.6, 2.0), (idle_end, -1.6)),
        "back_forearm": rotate((0, 0.8), (1.6, -1.9), (idle_end, 0.8)),
        "front_arm": rotate((0, 1.3), (1.6, -1.6), (idle_end, 1.3)),
        "front_forearm": rotate((0, -0.9), (1.6, 1.6), (idle_end, -0.9)),
        "front_hand": rotate((0, -1.3), (1.6, 2.0), (idle_end, -1.3)),
        "green_hair_1": rotate((0, -2.1), (1.6, 2.7), (idle_end, -2.1)),
        "green_hair_2": rotate((0, -3.1), (1.6, 3.8), (idle_end, -3.1)),
        "purple_hair_1": rotate((0, 1.7), (1.6, -2.4), (idle_end, 1.7)),
        "purple_hair_2": rotate((0, 2.8), (1.6, -3.5), (idle_end, 2.8)),
        "green_skirt_1": rotate((0, 1.1), (1.6, -1.3), (idle_end, 1.1)),
        "green_skirt_2": rotate((0, 1.9), (1.6, -2.4), (idle_end, 1.9)),
        "purple_skirt_1": rotate((0, -0.9), (1.6, 1.3), (idle_end, -0.9)),
        "purple_skirt_2": rotate((0, -1.7), (1.6, 2.3), (idle_end, -1.7)),
        "vine_1": rotate((0, -2.7), (1.6, 3.5), (idle_end, -2.7)),
        "vine_2": rotate((0, -4.0), (1.6, 5.1), (idle_end, -4.0)),
        "vine_3": rotate((0, -5.6), (1.6, 6.7), (idle_end, -5.6)),
    }

    attack_end = 0.42
    attack = {
        "pelvis": merge(
            translate((0, 0, 0), (0.16, 14, 2), (attack_end, 0, 0)),
            rotate((0, 0), (0.16, 3.0), (attack_end, 0)),
        ),
        "torso": rotate((0, 0), (0.16, -5.8), (attack_end, 0)),
        "head": rotate((0, 0), (0.16, 4.0), (attack_end, 0)),
        "back_arm": rotate((0, 0), (0.16, 6.5), (attack_end, 0)),
        "back_forearm": rotate((0, 0), (0.16, 9.0), (attack_end, 0)),
        "front_arm": rotate((0, 0), (0.16, -11.0), (attack_end, 0)),
        "front_forearm": rotate((0, 0), (0.16, -13.0), (attack_end, 0)),
        "front_hand": rotate((0, 0), (0.16, 10.0), (attack_end, 0)),
        "green_hair_2": rotate((0, 0), (0.2, 9.0), (attack_end, 0)),
        "purple_hair_2": rotate((0, 0), (0.2, 10.0), (attack_end, 0)),
        "green_skirt_2": rotate((0, 0), (0.2, 6.5), (attack_end, 0)),
        "purple_skirt_2": rotate((0, 0), (0.2, 7.5), (attack_end, 0)),
        "vine_1": rotate((0, 0), (0.16, -15.0), (attack_end, 0)),
        "vine_2": rotate((0, 0), (0.18, -23.0), (attack_end, 0)),
        "vine_3": rotate((0, 0), (0.2, -30.0), (attack_end, 0)),
    }

    hit_end = 0.36
    hit = {
        "pelvis": merge(
            translate((0, 0, 0), (0.12, -13, -4), (hit_end, 0, 0)),
            rotate((0, 0), (0.12, -4.5), (hit_end, 0)),
        ),
        "torso": rotate((0, 0), (0.12, 7.5), (hit_end, 0)),
        "head": rotate((0, 0), (0.14, 11.5), (hit_end, 0)),
        "back_arm": rotate((0, 0), (0.12, -10.0), (hit_end, 0)),
        "back_forearm": rotate((0, 0), (0.14, -12.5), (hit_end, 0)),
        "front_arm": rotate((0, 0), (0.12, 9.0), (hit_end, 0)),
        "front_forearm": rotate((0, 0), (0.14, 12.5), (hit_end, 0)),
        "green_hair_1": rotate((0, 0), (0.16, -10.0), (hit_end, 0)),
        "green_hair_2": rotate((0, 0), (0.18, -15.0), (hit_end, 0)),
        "purple_hair_1": rotate((0, 0), (0.16, -11.0), (hit_end, 0)),
        "purple_hair_2": rotate((0, 0), (0.18, -16.0), (hit_end, 0)),
        "green_skirt_2": rotate((0, 0), (0.18, -10.0), (hit_end, 0)),
        "purple_skirt_2": rotate((0, 0), (0.18, -11.5), (hit_end, 0)),
        "vine_1": rotate((0, 0), (0.15, 13.0), (hit_end, 0)),
        "vine_2": rotate((0, 0), (0.17, 18.0), (hit_end, 0)),
        "vine_3": rotate((0, 0), (0.19, 24.0), (hit_end, 0)),
    }
    return {"idle": {"bones": idle}, "attack": {"bones": attack}, "hit": {"bones": hit}}


def write_atlas(width: int, height: int) -> None:
    atlas = f"""{TEXTURE_NAME}
size: {width},{height}
format: RGBA8888
filter: Linear,Linear
repeat: none
muzixi
  rotate: false
  xy: 0, 0
  size: {width}, {height}
  orig: {width}, {height}
  offset: 0, 0
  index: -1
"""
    (OUTPUT_DIR / ATLAS_NAME).write_text(atlas, encoding="utf-8", newline="\n")


def main() -> None:
    texture = load_texture(SOURCE)
    configure_art_transform(texture)
    width, height = TEXTURE_SIZE

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    texture.save(OUTPUT_DIR / TEXTURE_NAME, "PNG", optimize=True)
    write_atlas(width, height)

    bones, world_positions = create_bones()
    bone_indexes = {bone["name"]: index for index, bone in enumerate(bones)}
    skeleton = {
        "skeleton": {
            "hash": "NineSwordTechniques-Muzixi",
            "spine": "3.4.02",
            "width": width,
            "height": height,
            "images": "./",
        },
        "bones": bones,
        "slots": [{"name": "character", "bone": "root", "attachment": "muzixi"}],
        "skins": {
            "default": {
                "character": {
                    "muzixi": create_mesh(bone_indexes, world_positions),
                }
            }
        },
        "animations": create_animations(),
    }
    destination = OUTPUT_DIR / SKELETON_NAME
    destination.write_text(
        json.dumps(skeleton, ensure_ascii=True, separators=(",", ":")),
        encoding="utf-8",
        newline="\n",
    )
    print(
        f"Generated {destination.relative_to(ROOT)} ({len(bones)} bones, "
        f"art scale {art_scale:.4f})"
    )


if __name__ == "__main__":
    main()
